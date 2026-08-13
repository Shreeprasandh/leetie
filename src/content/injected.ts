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
        // Priority 1: Active editor instances
        const editors = monaco.editor.getEditors?.();
        if (editors && editors.length > 0) {
          for (const ed of editors) {
            const val = ed.getModel?.()?.getValue?.();
            if (val && val.trim()) return val;
          }
        }
        // Priority 2: All models in memory — find first non-empty code model
        const models = monaco.editor.getModels?.();
        if (models && models.length > 0) {
          for (const model of models) {
            const val = model.getValue?.();
            // Skip empty models, JSON schemas, or internal CSS
            if (val && val.trim() && !model.uri?.path?.endsWith('.json')) {
              return val;
            }
          }
        }
      }

      // Priority 3: Fallback DOM textareas & view lines
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

    // Only exclude explicit "Run Code" test endpoints
    if (url.includes('/interpret_solution/')) {
      return false;
    }

    // Shape 1 — GraphQL submissionDetails / submissionResult / submitCode
    const gql =
      data?.data?.submissionDetails ||
      data?.data?.submissionResult ||
      data?.data?.submitCode;
    if (gql) {
      const status = gql.statusDisplay || gql.status_display || gql.status_msg;
      if (status === 'Accepted') return true;
    }

    // Shape 2 — REST polling check endpoint or flat response
    const status = data?.status_msg || data?.statusDisplay || data?.status_display;
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

      // Ignore explicit interpret_solution endpoints
      if (!url.includes('/interpret_solution/')) {
        const clone = response.clone();

        clone
          .json()
          .then((data: any) => {
            if (!isAcceptedResponse(data, url)) return;

            // Extract code from Monaco editor or DOM
            const gql =
              data?.data?.submissionDetails ||
              data?.data?.submissionResult ||
              data?.data?.submitCode;
            const details = gql || data;

            // Extract numeric submission ID from URL if available
            const urlMatch = url.match(/submissions\/(?:detail\/)?(\d+)/);
            const extractedId = urlMatch?.[1] || details?.submission_id || details?.id || details?.submissionId;
            if (extractedId) {
              data._submission_id = String(extractedId);
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

            console.log('[leetie] Accepted submission intercepted and dispatched:', data._submission_id || 'detected');
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
