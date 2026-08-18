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

      // Priority 3: CodeMirror 6 editor (used in LeetCode dynamic layout updates)
      const cmLines = document.querySelectorAll('.cm-content .cm-line');
      if (cmLines.length > 0) {
        const codeText = Array.from(cmLines).map((l) => l.textContent || '').join('\n');
        if (codeText.trim()) return codeText;
      }

      // Priority 4: Monaco DOM view lines
      const viewLines = document.querySelectorAll('.view-lines .view-line, .view-line');
      if (viewLines.length > 0) {
        const codeText = Array.from(viewLines).map((l) => l.textContent || '').join('\n');
        if (codeText.trim()) return codeText;
      }

      // Priority 5: Fallback DOM textareas
      const textarea = document.querySelector('textarea.inputarea, textarea[aria-label*="code"]') as HTMLTextAreaElement;
      if (textarea && textarea.value && textarea.value.trim().length > 10) {
        return textarea.value;
      }
    } catch (_) { /* ignore */ }
    return '';
  }

  // -------------------------------------------------------------------------
  // Response shape detection helpers
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // Response shape detection helpers
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // Response shape detection helpers
  // -------------------------------------------------------------------------
  function isAcceptedResponse(data: any, url: string): boolean {
    if (!data) return false;

    // Explicitly exclude "Run Code" test runs (interpret_solution endpoint or interpret payload keys)
    if (url.includes('/interpret_solution/')) return false;
    if (data?.interpret_id || data?.interpret_key || data?.data?.interpret_id || data?.data?.interpret_key) return false;

    // Shape 1 — GraphQL (submissionCheck, submissionDetails, submissionResult, submitCode, or any data operation)
    const gql =
      data?.data?.submissionCheck ||
      data?.data?.submissionDetails ||
      data?.data?.submissionResult ||
      data?.data?.submitCode ||
      (data?.data && typeof data.data === 'object' ? (Object.values(data.data)[0] as any) : null);

    if (gql) {
      if (gql.interpret_id || gql.interpret_key || (typeof gql.task_id === 'string' && gql.task_id.includes('interpret'))) {
        return false;
      }
      const status = gql.statusDisplay || gql.status_display || gql.status_msg || gql.status;
      if (status && String(status).toLowerCase().includes('accepted')) return true;
    }

    // Shape 2 — REST polling check endpoint or flat response
    const status = data?.status_msg || data?.statusDisplay || data?.status_display || data?.status;
    return Boolean(status && String(status).toLowerCase().includes('accepted'));
  }

  function extractLanguageFromDOM(): string {
    try {
      const win = window as any;
      const monaco = win.monaco || win._monaco;
      if (monaco?.editor) {
        const models = monaco.editor.getModels?.();
        if (models && models.length > 0) {
          for (const model of models) {
            const langId = model.getLanguageId?.();
            if (langId && !['plaintext', 'json', 'css', 'html'].includes(langId)) {
              const norm = normalizeLang(langId);
              if (norm) return norm;
            }
          }
        }
      }

      const langSelectors = document.querySelectorAll(
        'button[id*="lang"], [data-cy="lang-select"], [class*="language-select"], [class*="lang-select"], [class*="ant-select-selection-selected-value"]'
      );
      for (const el of Array.from(langSelectors)) {
        const txt = el.textContent?.trim() || '';
        if (txt) {
          const norm = normalizeLang(txt);
          if (norm) return norm;
        }
      }

      const buttons = document.querySelectorAll('button, div[class*="select"]');
      for (const btn of Array.from(buttons)) {
        const txt = btn.textContent?.trim() || '';
        if (txt && txt.length < 30) {
          const norm = normalizeLang(txt);
          if (norm) return norm;
        }
      }
    } catch (_) { /* ignore */ }
    return '';
  }

  function normalizeLang(raw: string): string {
    const s = raw.toLowerCase().replace(/[\s\-_]/g, '');
    if (s.includes('mysql') || s.includes('sql')) return 'mysql';
    if (s.includes('python3') || s.includes('python')) return 'python3';
    if (s.includes('javascript') || s === 'js') return 'javascript';
    if (s.includes('typescript') || s === 'ts') return 'typescript';
    if (s.includes('cpp') || s.includes('c++')) return 'cpp';
    if (s === 'c') return 'c';
    if (s.includes('java') && !s.includes('javascript')) return 'java';
    if (s.includes('csharp') || s.includes('c#')) return 'csharp';
    if (s.includes('golang') || s === 'go') return 'golang';
    if (s.includes('rust') || s === 'rs') return 'rust';
    if (s.includes('kotlin') || s === 'kt') return 'kotlin';
    if (s.includes('swift')) return 'swift';
    if (s.includes('ruby') || s === 'rb') return 'ruby';
    if (s.includes('postgresql')) return 'postgresql';
    if (s.includes('oraclesql')) return 'oraclesql';
    if (s.includes('mssql')) return 'mssql';
    if (s.includes('scala')) return 'scala';
    if (s.includes('php')) return 'php';
    if (s.includes('dart')) return 'dart';
    if (s.includes('elixir')) return 'elixir';
    return '';
  }

  const _dispatchedSubmissionIds = new Set<string>();

  function dispatchSubmission(data: any, url: string, isDomFallback = false) {
    try {
      const gql =
        data?.data?.submissionCheck ||
        data?.data?.submissionDetails ||
        data?.data?.submissionResult ||
        data?.data?.submitCode ||
        (data?.data && typeof data.data === 'object' ? (Object.values(data.data)[0] as any) : null);
      const details = gql || data;

      // Extract numeric submission ID from URL or payload if available
      const urlMatch = url.match(/submissions\/(?:detail\/)?(\d+)/);
      const extractedId = urlMatch?.[1] || details?.submission_id || details?.id || details?.submissionId || details?.question_id;
      const subId = extractedId ? String(extractedId) : (details._submission_id || `sub_${Date.now()}`);

      // Skip if this exact submission ID was already dispatched in this page session
      if (_dispatchedSubmissionIds.has(subId)) {
        return;
      }
      _dispatchedSubmissionIds.add(subId);
      data._submission_id = subId;

      // Ensure lang is properly populated from payload or DOM extraction
      const domLang = extractLanguageFromDOM();
      if (!details.lang && !details.language && domLang) {
        details.lang = domLang;
        data.lang = domLang;
      }

      const code = details?.code || extractMonacoCode();

      if (!code) {
        console.warn('[leetie] Accepted submission found but could not extract code.');
        _dispatchedSubmissionIds.delete(subId); // Allow retry if code extraction failed
        return;
      }

      // Dispatch to isolated world via shared DOM event
      window.dispatchEvent(
        new CustomEvent(LEETIE_EVENT, {
          detail: { rawData: data, code },
        })
      );

      console.log(`[leetie] Accepted submission intercepted via ${isDomFallback ? 'DOM Observer' : 'Network Interceptor'}:`, subId);
    } catch (err) {
      console.warn('[leetie] Error processing accepted submission event:', err);
    }
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

      if (!url.includes('/interpret_solution/')) {
        const clone = response.clone();

        clone
          .json()
          .then((data: any) => {
            if (!isAcceptedResponse(data, url)) return;
            dispatchSubmission(data, url, false);
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

  // -------------------------------------------------------------------------
  // XHR interceptor
  // -------------------------------------------------------------------------
  const originalXHROpen = XMLHttpRequest.prototype.open;
  const originalXHRSend = XMLHttpRequest.prototype.send;

  XMLHttpRequest.prototype.open = function (method: string, url: string | URL, ...rest: any[]) {
    (this as any)._leetie_url = typeof url === 'string' ? url : (url as URL)?.href || '';
    return originalXHROpen.apply(this, [method, url, ...rest] as any);
  };

  XMLHttpRequest.prototype.send = function (...args: any[]) {
    this.addEventListener('load', function () {
      try {
        const url = (this as any)._leetie_url || '';
        if (!url.includes('/interpret_solution/')) {
          const text = this.responseText;
          if (text && text.startsWith('{')) {
            const data = JSON.parse(text);
            if (isAcceptedResponse(data, url)) {
              dispatchSubmission(data, url, false);
            }
          }
        }
      } catch (_) { /* ignore */ }
    });
    return originalXHRSend.apply(this, args as any);
  };

  // -------------------------------------------------------------------------
  // DOM Observer Fallback for "Accepted" Status Card
  // -------------------------------------------------------------------------
  let _lastDomAcceptedTime = 0;
  const domObserver = new MutationObserver(() => {
    try {
      // Do not trigger DOM fallback when browsing submission history lists or discussion tabs
      if (window.location.pathname.includes('/submissions/')) return;

      const now = Date.now();
      if (now - _lastDomAcceptedTime < 5000) return; // Debounce

      const resultBadge = document.querySelector(
        '[data-e2e-locator="submission-result"], div[class*="result-container"] [class*="status-accepted"], div[class*="result-state"]'
      );
      if (resultBadge && resultBadge.textContent?.trim().toLowerCase() === 'accepted') {
        const code = extractMonacoCode();
        if (code) {
          _lastDomAcceptedTime = now;
          const detectedLang = extractLanguageFromDOM() || 'python3';
          const syntheticPayload = {
            status_msg: 'Accepted',
            statusDisplay: 'Accepted',
            lang: detectedLang,
            language: detectedLang,
            code,
            _submission_id: `dom_${now}`,
          };
          dispatchSubmission(syntheticPayload, window.location.href, true);
        }
      }
    } catch (_) { /* ignore */ }
  });

  if (document.body) {
    domObserver.observe(document.body, { childList: true, subtree: true });
  } else {
    document.addEventListener('DOMContentLoaded', () => {
      if (document.body) domObserver.observe(document.body, { childList: true, subtree: true });
    });
  }

  console.log('[leetie] Main-world fetch, XHR, and DOM interceptors active.');
})();
