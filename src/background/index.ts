import { storage } from '../shared/storage';
import { Message } from '../shared/messages';
import { Submission } from '../shared/types';
import { GitHubService } from './github.service';
import { SyncService } from './sync.service';
import { initiateOAuthFlow } from './auth.service';

console.log('[leetie] Background service worker initialized.');

// ---------------------------------------------------------------------------
// Service Worker Keep-Alive
// MV3 service workers are terminated after ~30s of inactivity. During a long
// commit, the SW must stay alive. A lightweight getPlatformInfo
// ping every 20s prevents termination without any meaningful overhead.
// ---------------------------------------------------------------------------
let _keepAliveTimer: ReturnType<typeof setInterval> | null = null;

function keepAlive() {
  if (_keepAliveTimer) return;
  _keepAliveTimer = setInterval(() => {
    chrome.runtime.getPlatformInfo(() => { void chrome.runtime.lastError; });
  }, 20_000);
}

function releaseKeepAlive() {
  if (_keepAliveTimer) {
    clearInterval(_keepAliveTimer);
    _keepAliveTimer = null;
  }
}

// Tracks submissions currently being committed to prevent duplicate commits
// when both the check endpoint and the GraphQL submissionDetails query fire
// for the same accepted submission in quick succession.
const inFlightSubmissions = new Set<string>();

async function processPendingQueue() {
  try {
    const pending = await storage.getPendingSubmissions();
    if (!pending.length) return;
    console.log(`[leetie] Processing ${pending.length} pending submission(s)...`);
    for (const sub of pending) {
      try {
        await SyncService.commitSubmission(sub);
        await storage.removePendingSubmission(sub.submissionId);
        console.log('[leetie] Pending submission successfully committed:', sub.problem.title);
      } catch (err: any) {
        console.warn('[leetie] Pending submission retry skipped:', sub.problem.title, err.message);
      }
    }
  } catch (err) {
    console.warn('[leetie] Error processing pending queue:', err);
  }
}

// Process any queued offline submissions on service worker startup
processPendingQueue();

if (typeof chrome !== 'undefined' && chrome.runtime?.onMessage) {
  chrome.runtime.onMessage.addListener((message: Message, _sender, sendResponse) => {
    if (message.type === 'GET_STATE') {
      storage.getState().then((state) => sendResponse(state));
      return true;
    }

    if (message.type === 'SYNC_GITHUB') {
      (async () => {
        try {
          await storage.setState({ syncStatus: 'syncing', lastError: null });
          const res = await SyncService.syncFromRepo();
          await storage.setState({ syncStatus: 'idle', lastError: null });
          sendResponse({ success: true, count: res.count });
        } catch (err: any) {
          await storage.setState({ syncStatus: 'error', lastError: err.message });
          sendResponse({ success: false, error: err.message });
        }
      })();
      return true;
    }

    if (message.type === 'TEST_CONNECTION') {
      const { token, username, repoName } = message.payload as { token: string; username: string; repoName: string };
      (async () => {
        try {
          const user = await GitHubService.verifyUser(token);
          const targetUsername = username || user.login;
          const repoOk = await GitHubService.ensureRepo(token, targetUsername, repoName);
          await storage.setConfig({ githubToken: token, githubUsername: targetUsername });
          await storage.setState({ isAuthenticated: true, lastError: null });
          processPendingQueue();
          sendResponse({ success: true, user, repoOk });
        } catch (err: any) {
          await storage.setState({ isAuthenticated: false, lastError: err.message });
          sendResponse({ success: false, error: err.message });
        }
      })();
      return true;
    }

    if (message.type === 'START_OAUTH') {
      (async () => {
        keepAlive();
        try {
          const config = await storage.getConfig();
          const token = await initiateOAuthFlow(
            import.meta.env.VITE_GITHUB_CLIENT_ID || 'Ov23li296L9RxwhuLXOv',
            config.proxyUrl
          );
          const user = await GitHubService.verifyUser(token);
          await GitHubService.ensureRepo(token, user.login, config.repoName);
          await storage.setConfig({ githubToken: token, githubUsername: user.login });
          await storage.setState({ isAuthenticated: true, lastError: null });
          processPendingQueue();
          sendResponse({ success: true, user });
        } catch (err: any) {
          await storage.setState({ isAuthenticated: false, lastError: err.message });
          sendResponse({ success: false, error: err.message });
        } finally {
          releaseKeepAlive();
        }
      })();
      return true;
    }

    if (message.type === 'FETCH_REPOS') {
      const { token } = message.payload as { token: string };
      GitHubService.getUserRepos(token)
        .then((repos) => sendResponse({ success: true, repos }))
        .catch((err) => sendResponse({ success: false, error: err.message }));
      return true;
    }

    if (message.type === 'SUBMISSION_DETECTED') {
      const submission = message.payload as Submission;

      // Drop duplicate events for the same submission (check endpoint + GraphQL can both fire)
      if (inFlightSubmissions.has(submission.submissionId)) {
        console.log('[leetie] Duplicate submission event ignored:', submission.submissionId);
        sendResponse({ success: false, error: 'Already processing this submission.' });
        return true;
      }
      inFlightSubmissions.add(submission.submissionId);

      console.log('[leetie] Background worker orchestrating commit for:', submission);
      (async () => {
        keepAlive(); // Prevent SW termination during async commit
        try {
          await storage.setState({ syncStatus: 'syncing' });
          const record = await SyncService.commitSubmission(submission);
          await storage.removePendingSubmission(submission.submissionId);
          await storage.setState({ syncStatus: 'idle', lastError: null });

          if (typeof chrome !== 'undefined' && chrome.notifications) {
            try {
              const iconUrl = chrome.runtime?.getURL ? chrome.runtime.getURL('assets/icon-48.png') : 'assets/icon-48.png';
              chrome.notifications.create({
                type: 'basic',
                iconUrl,
                title: 'leetie — Solution Synced!',
                message: `Successfully committed ${submission.problem.id}. ${submission.problem.title} [${submission.problem.difficulty}] to GitHub.`,
              });
            } catch (nErr) {
              console.warn('[leetie] Notification creation skipped:', nErr);
            }
          }

          sendResponse({ success: true, record });
        } catch (err: any) {
          await storage.addPendingSubmission(submission);
          await storage.setState({ syncStatus: 'error', lastError: err.message });
          sendResponse({ success: false, error: err.message });
        } finally {
          inFlightSubmissions.delete(submission.submissionId);
          releaseKeepAlive(); // Allow SW to sleep once work is done
        }
      })();
      return true;
    }

    return false;
  });
}
