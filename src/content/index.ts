import { Message } from '../shared/messages';
import { LeetCodeExtractor } from './extractor';
import { Submission } from '../shared/types';

console.log('[leetie] Isolated-world content script active on LeetCode.');

// ---------------------------------------------------------------------------
// SUBMISSION BRIDGE
// ---------------------------------------------------------------------------
// content/injected.ts (MAIN world) intercepts window.fetch and dispatches
// a '__leetie_submission__' CustomEvent on the shared DOM window when an
// accepted submission is detected.
//
// This isolated-world script receives that event and forwards the submission
// to the background service worker via chrome.runtime.sendMessage.
//
// We CANNOT use setupFetchInterceptor here because isolated-world window.fetch
// is a completely separate function reference from the page's fetch — overriding
// it here has zero effect on LeetCode's own HTTP calls.
// ---------------------------------------------------------------------------
const LEETIE_EVENT = '__leetie_submission__';

window.addEventListener(LEETIE_EVENT, (e: Event) => {
  const { rawData, code } = (e as CustomEvent<{ rawData: any; code: string }>).detail;

  // Parse the raw response into a typed Submission object
  const submission: Submission | null = LeetCodeExtractor.parseSubmissionResponse(rawData, code);

  if (!submission) {
    console.warn('[leetie] Could not parse submission from main-world event.');
    return;
  }

  console.log('[leetie] Forwarding accepted submission to background:', submission.problem.title);

  if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
    const msg: Message = { type: 'SUBMISSION_DETECTED', payload: submission };
    chrome.runtime.sendMessage(msg, (response) => {
      if (chrome.runtime.lastError) {
        console.warn('[leetie] Background not available:', chrome.runtime.lastError.message);
        return;
      }
      if (response?.success) {
        console.log('[leetie] Background confirmed commit:', response);
      }
    });
  }
});

// ---------------------------------------------------------------------------
// PROXY_GRAPHQL handler
// ---------------------------------------------------------------------------
// The background recovery service sends GraphQL requests here to be executed
// in the page context where the user's LeetCode session cookies are active.
// MV3 service workers cannot use credentials:include — only content scripts can.
// ---------------------------------------------------------------------------
const nativeFetch = window.fetch.bind(window);

chrome.runtime.onMessage.addListener((message: Message, sender, sendResponse) => {
  // Only accept messages from our own extension background
  if (sender.id !== chrome.runtime.id) return false;

  if (message.type === 'PROXY_GRAPHQL') {
    const { body } = message.payload as { body: string };

    const csrfToken =
      document.cookie
        .split(';')
        .find((c) => c.trim().startsWith('csrftoken='))
        ?.split('=')?.[1]
        ?.trim() || '';

    nativeFetch('https://leetcode.com/graphql', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Referer': 'https://leetcode.com',
        'Origin': 'https://leetcode.com',
        'x-requested-with': 'XMLHttpRequest',
        ...(csrfToken ? { 'x-csrftoken': csrfToken } : {}),
      },
      credentials: 'include',
      body,
    })
      .then((res) => res.json())
      .then((data) => sendResponse({ success: true, data }))
      .catch((err) => sendResponse({ success: false, error: (err as Error).message }));

    return true; // Keep message channel open for async response
  }

  return false;
});
