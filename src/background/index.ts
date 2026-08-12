import { storage } from '../shared/storage';
import { Message } from '../shared/messages';

console.log('[leetie] Background service worker initialized.');

// Listen for messages from content script or popup UI
if (typeof chrome !== 'undefined' && chrome.runtime?.onMessage) {
  chrome.runtime.onMessage.addListener((message: Message, _sender, sendResponse) => {
    if (message.type === 'GET_STATE') {
      storage.getState().then((state) => sendResponse(state));
      return true; // Async response
    }
    return false;
  });
}
