import { storage } from '../shared/storage';
import { Message } from '../shared/messages';
import { Submission } from '../shared/types';
import { GitHubService } from './github.service';
import { SyncService } from './sync.service';
import { RecoveryService } from './recovery.service';
import { initiateOAuthFlow } from './auth.service';

console.log('[leetie] Background service worker initialized.');

if (typeof chrome !== 'undefined' && chrome.runtime?.onMessage) {
  chrome.runtime.onMessage.addListener((message: Message, _sender, sendResponse) => {
    if (message.type === 'GET_STATE') {
      storage.getState().then((state) => sendResponse(state));
      return true;
    }

    if (message.type === 'RECOVERY_START') {
      RecoveryService.startRecovery();
      sendResponse({ success: true, message: 'History recovery started.' });
      return true;
    }

    if (message.type === 'RECOVERY_STOP') {
      RecoveryService.stopRecovery();
      sendResponse({ success: true, message: 'History recovery stopped.' });
      return true;
    }

    if (message.type === 'TEST_CONNECTION') {
      const { token, repoName } = message.payload as { token: string; username: string; repoName: string };
      (async () => {
        try {
          const user = await GitHubService.verifyUser(token);
          const repoOk = await GitHubService.ensureRepo(token, user.login, repoName);
          await storage.setState({ isAuthenticated: true, lastError: null });
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
        try {
          const config = await storage.getConfig();
          const token = await initiateOAuthFlow(
            import.meta.env.VITE_GITHUB_CLIENT_ID || 'Ov23li296L9RxwhuLXOv',
            config.proxyUrl
          );
          const user = await GitHubService.verifyUser(token);
          await storage.setConfig({ githubToken: token, githubUsername: user.login });
          await GitHubService.ensureRepo(token, user.login, config.repoName);
          await storage.setState({ isAuthenticated: true, lastError: null });
          sendResponse({ success: true, user });
        } catch (err: any) {
          await storage.setState({ isAuthenticated: false, lastError: err.message });
          sendResponse({ success: false, error: err.message });
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
      console.log('[leetie] Background worker orchestrating commit for:', submission);
      (async () => {
        try {
          await storage.setState({ syncStatus: 'syncing' });
          const record = await SyncService.commitSubmission(submission);
          await storage.setState({ syncStatus: 'idle', lastError: null });

          if (typeof chrome !== 'undefined' && chrome.notifications) {
            chrome.notifications.create({
              type: 'basic',
              iconUrl: 'assets/icon-48.png',
              title: 'leetie — Solution Synced!',
              message: `Successfully committed ${submission.problem.id}. ${submission.problem.title} [${submission.problem.difficulty}] to GitHub.`,
            });
          }

          sendResponse({ success: true, record });
        } catch (err: any) {
          await storage.setState({ syncStatus: 'error', lastError: err.message });
          sendResponse({ success: false, error: err.message });
        }
      })();
      return true;
    }

    return false;
  });
}
