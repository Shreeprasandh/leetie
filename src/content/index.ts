import { setupFetchInterceptor } from './interceptor';
import { Message } from '../shared/messages';

console.log('[leetie] Content script active on LeetCode.');

setupFetchInterceptor((submission) => {
  if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
    const msg: Message = {
      type: 'SUBMISSION_DETECTED',
      payload: submission,
    };
    chrome.runtime.sendMessage(msg, (response) => {
      if (response?.success) {
        console.log('[leetie] Background confirmed submission receipt:', response);
      }
    });
  }
});
