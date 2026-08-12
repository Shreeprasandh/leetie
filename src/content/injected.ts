// ---------------------------------------------------------------------------
// leetie — MAIN WORLD content script
// ---------------------------------------------------------------------------
// This file runs in the page's MAIN JavaScript world (world: "MAIN" in manifest).
// Only from here can we:
//   1. Override window.fetch to see LeetCode's own HTTP requests
//   2. Access window.monaco to read the editor's current code
//
// Chrome's isolated world (content/index.ts) cannot do either of these.
// Communication back to the isolated world uses window.dispatchEvent with
// a CustomEvent on the shared DOM window object.
// ---------------------------------------------------------------------------

(function () {
  'use strict';

  const LEETIE_EVENT = '__leetie_submission__';

  // -------------------------------------------------------------------------
  // Monaco code extractor — only works in main world
  // -------------------------------------------------------------------------
  function extractMonacoCode(): string {
    try {
      const win = window as any;
      // Monaco may be under window.monaco or window._monaco
      const monaco = win.monaco || win._monaco;
      if (monaco?.editor) {
        const models = monaco.editor.getModels();
        if (models && models.length > 0) {
          const val = models[0].getValue();
          if (val) return val;
        }
      }

      // Fallback: check DOM editor containers or textareas if Monaco model is empty
      const textarea = document.querySelector('textarea.inputarea, textarea[aria-label*="code"]') as HTMLTextAreaElement;
      if (textarea && textarea.value) return textarea.value;

      const viewLines = document.querySelectorAll('.view-line');
      if (viewLines.length > 0) {
        return Array.from(viewLines).map((l) => l.textContent || '').join('\n');
      }
    } catch (_) { /* ignore */ }
    return '';
  }

  // -------------------------------------------------------------------------
  // Response shape detection helpers
  // -------------------------------------------------------------------------
  function isAcceptedResponse(data: any, url: string): boolean {
    if (!data) return false;

    // Filter out "Run Code" sample test runs
    if (url.includes('/interpret') || url.includes('/runcode/')) {
      return false;
    }
    if (data.interpret_id != null || data.interpret_code != null || data.task_name === 'interpret') {
      return false;
    }

    // Shape 1 — GraphQL submissionDetails
    const gql = data?.data?.submissionDetails || data?.data?.submissionResult;
    if (gql) {
      const status = gql.statusDisplay || gql.status_display;
      return status === 'Accepted';
    }

    // Shape 2 — REST polling endpoint (/submissions/detail/{id}/check/)
    const status = data?.status_msg || data?.statusDisplay;
    return status === 'Accepted';
  }

  // -------------------------------------------------------------------------
  // Fetch interceptor
  // -------------------------------------------------------------------------
  const originalFetch = window.fetch.bind(window);

  window.fetch = async function (...args: Parameters<typeof fetch>): ReturnType<typeof fetch> {
    const response = await originalFetch(...args);

    try {
      const rawUrl = args[0];
      const url =
        typeof rawUrl === 'string'
          ? rawUrl
          : rawUrl instanceof URL
          ? rawUrl.href
          : (rawUrl as Request)?.url || '';

      // Only inspect submission-related endpoints, ignore interpret/test runs
      if (
        (url.includes('/graphql') || url.includes('/submissions/detail/') || url.includes('/check/')) &&
        !url.includes('/interpret') &&
        !url.includes('/runcode/')
      ) {
        const clone = response.clone();

        clone
          .json()
          .then((data: any) => {
            if (!isAcceptedResponse(data, url)) return;

            // Try to get code from the response first; fall back to Monaco
            const gql = data?.data?.submissionDetails || data?.data?.submissionResult;
            const details = gql || data;

            // Extract numeric submission ID from URL if missing from body
            const urlMatch = url.match(/\/submissions\/detail\/(\d+)\/check/);
            if (urlMatch && urlMatch[1] && !details.submission_id && !details.id && !details.submissionId) {
              details.submission_id = urlMatch[1];
            }

            const code = details?.code || extractMonacoCode();

            if (!code) {
              console.warn('[leetie] Accepted submission found but could not extract code.');
              return;
            }

            // Dispatch to isolated world via shared DOM event
            window.dispatchEvent(
              new CustomEvent(LEETIE_EVENT, {
                detail: { rawData: data, code },
              })
            );

            console.log('[leetie] Accepted submission dispatched from main world:', details.submission_id || 'detected');
          })
          .catch(() => {
            // Non-JSON response — ignore silently
          });
      }
    } catch (_) {
      // Never interrupt LeetCode's core functionality
    }

    return response;
  };

  console.log('[leetie] Main-world fetch interceptor active.');
})();
