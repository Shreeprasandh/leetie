import { storage } from '../shared/storage';
import { Message } from '../shared/messages';
import { GitHubService } from './github.service';

console.log('[leetie] Background service worker initialized.');

if (typeof chrome !== 'undefined' && chrome.runtime?.onMessage) {
  chrome.runtime.onMessage.addListener((message: Message, _sender, sendResponse) => {
    if (message.type === 'GET_STATE') {
      storage.getState().then((state) => sendResponse(state));
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

    if (message.type === 'FETCH_REPOS') {
      const { token } = message.payload as { token: string };
      GitHubService.getUserRepos(token)
        .then((repos) => sendResponse({ success: true, repos }))
        .catch((err) => sendResponse({ success: false, error: err.message }));
      return true;
    }

    return false;
  });
}
