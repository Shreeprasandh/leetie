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

// Send message to background service worker with retry to wake up dormant MV3 worker
async function sendMessageToBackgroundWithRetry(msg: Message, retries = 3, delayMs = 600): Promise<any> {
  for (let attempt = 1; attempt <= retries; attempt++) {
    try {
      const response = await new Promise<any>((resolve, reject) => {
        if (typeof chrome === 'undefined' || !chrome.runtime?.sendMessage) {
          return reject(new Error('Chrome extension runtime not available.'));
        }
        chrome.runtime.sendMessage(msg, (res) => {
          if (chrome.runtime.lastError) {
            return reject(new Error(chrome.runtime.lastError.message));
          }
          resolve(res);
        });
      });
      return response;
    } catch (err: any) {
      console.warn(`[leetie] Background message attempt ${attempt}/${retries} failed:`, err.message);
      if (attempt < retries) {
        await new Promise((r) => setTimeout(r, delayMs));
      } else {
        throw err;
      }
    }
  }
}

window.addEventListener(LEETIE_EVENT, async (e: Event) => {
  const { rawData, code } = (e as CustomEvent<{ rawData: any; code: string }>).detail;

  // Parse the raw response into a typed Submission object with metadata fallback
  const submission: Submission | null = await LeetCodeExtractor.parseSubmissionResponseAsync(rawData, code);

  if (!submission) {
    console.warn('[leetie] Could not parse submission from main-world event.');
    return;
  }

  console.log('[leetie] Forwarding accepted submission to background:', submission.problem.title);

  const msg: Message = { type: 'SUBMISSION_DETECTED', payload: submission };
  try {
    const response = await sendMessageToBackgroundWithRetry(msg);
    if (response?.success) {
      console.log('[leetie] Background confirmed commit:', response);
    }
  } catch (err: any) {
    console.error('[leetie] Failed to send submission to background service worker:', err);
  }
});

// Real-time auth sync: Listen to chrome.storage changes so open tabs pick up credentials instantly
if (typeof chrome !== 'undefined' && chrome.storage?.onChanged) {
  chrome.storage.onChanged.addListener((changes, areaName) => {
    if (areaName === 'local' && (changes.leetie_config || changes.leetie_state)) {
      console.log('[leetie] Configuration updated in storage, active session synchronized.');
    }
  });
}

// ---------------------------------------------------------------------------
// PROXY_GRAPHQL handler
// ---------------------------------------------------------------------------
// The background recovery service sends GraphQL requests here to be executed
// in the page context where the user's LeetCode session cookies are active.
// MV3 service workers cannot use credentials:include — only content scripts can.
// ---------------------------------------------------------------------------
const nativeFetch = window.fetch.bind(window);

chrome.runtime.onMessage.addListener((message: Message, _sender, sendResponse) => {
  if (message?.type === 'PROXY_GRAPHQL') {
    const { body } = message.payload as { body: string };

    const csrfMatch = document.cookie.match(/(?:^|;\s*)csrftoken=([^;]+)/);
    const csrfToken = csrfMatch ? csrfMatch[1].trim() : '';

    const fetchGql = (url: string) =>
      nativeFetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Referer': 'https://leetcode.com/',
          'Origin': 'https://leetcode.com',
          'x-requested-with': 'XMLHttpRequest',
          ...(csrfToken ? { 'x-csrftoken': csrfToken } : {}),
        },
        credentials: 'include',
        body,
      });

    fetchGql('https://leetcode.com/graphql/')
      .then(async (res) => {
        if (!res.ok) {
          // Retry without trailing slash if 400/404
          const retryRes = await fetchGql('https://leetcode.com/graphql');
          if (!retryRes.ok) {
            if (retryRes.status === 403 || res.status === 403) {
              throw new Error('LeetCode session expired or CSRF token invalid. Please refresh your LeetCode tab.');
            }
            const errText = await retryRes.text().catch(() => '');
            let parsedDetail = '';
            try {
              const j = JSON.parse(errText);
              if (j.errors?.[0]?.message) parsedDetail = j.errors[0].message;
              else if (j.error) parsedDetail = j.error;
            } catch {
              parsedDetail = errText.slice(0, 100);
            }
            throw new Error(`LeetCode GraphQL returned HTTP ${retryRes.status}${parsedDetail ? `: ${parsedDetail}` : ''}`);
          }
          return retryRes.json();
        }
        return res.json();
      })
      .then((data) => sendResponse({ success: true, data }))
      .catch((err) => sendResponse({ success: false, error: (err as Error).message }));

    return true; // Keep message channel open for async response
  }

  return false;
});
