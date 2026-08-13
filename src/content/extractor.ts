import { Difficulty, Problem, Submission } from '../shared/types';

export class LeetCodeExtractor {
  static extractProblemSlug(): string {
    const pathParts = window.location.pathname.split('/').filter(Boolean);
    // e.g. /problems/two-sum/
    const problemsIndex = pathParts.indexOf('problems');
    if (problemsIndex !== -1 && pathParts[problemsIndex + 1]) {
      return pathParts[problemsIndex + 1];
    }
    return '';
  }

  static extractFromDOM(): Partial<Problem> {
    const slug = this.extractProblemSlug();

    // Strip both '- LeetCode' (legacy) and '| LeetCode' (current) title formats
    let title = document.title.replace(/[|\-]\s*LeetCode\s*$/i, '').trim();
    let id = '0';

    const match = title.match(/^(\d+)\.\s*(.+)$/);
    if (match) {
      id = match[1];
      title = match[2];
    } else {
      // Check DOM elements for title header like "2958. Length of Longest..."
      const candidates = document.querySelectorAll('h4, [data-cy="question-title"], .text-title-large, a[href*="/problems/"]');
      for (const el of Array.from(candidates)) {
        // If candidate is an anchor, ensure it points to the current problem slug
        const anchor = el.tagName === 'A' ? (el as HTMLAnchorElement) : el.querySelector('a');
        if (anchor && anchor.href && slug && !anchor.href.includes(`/problems/${slug}`)) {
          continue; // Skip unrelated "Similar Problems" links
        }
        const text = el.textContent?.trim() || '';
        const h4Match = text.match(/^(\d+)\.\s*(.+)$/);
        if (h4Match) {
          id = h4Match[1];
          title = h4Match[2];
          break;
        }
      }
    }

    // Difficulty extraction from page DOM elements
    let difficulty: Difficulty = 'Easy';
    const diffElement = document.querySelector(
      '[class*="text-difficulty-"], [class*="text-sd-easy"], [class*="text-sd-medium"], [class*="text-sd-hard"], [class*="text-easy"], [class*="text-medium"], [class*="text-hard"], div[class*="difficulty"]'
    );
    if (diffElement && diffElement.textContent) {
      const text = diffElement.textContent.trim().toLowerCase();
      if (text.includes('medium')) difficulty = 'Medium';
      else if (text.includes('hard')) difficulty = 'Hard';
      else if (text.includes('easy')) difficulty = 'Easy';
    } else {
      const bodyText = document.body?.innerText || '';
      if (bodyText.includes('Medium')) difficulty = 'Medium';
      else if (bodyText.includes('Hard')) difficulty = 'Hard';
    }

    return {
      id,
      slug,
      title,
      difficulty,
      topicTags: [],
    };
  }

  static extractFromMonaco(): string | null {
    try {
      const win = window as any;
      const monaco = win.monaco || win._monaco;
      if (monaco?.editor) {
        const editors = monaco.editor.getEditors?.();
        if (editors && editors.length > 0) {
          for (const ed of editors) {
            const val = ed.getModel?.()?.getValue?.();
            if (val && val.trim()) return val;
          }
        }
        const models = monaco.editor.getModels?.();
        if (models && models.length > 0) {
          for (const model of models) {
            const val = model.getValue?.();
            if (val && val.trim() && !model.uri?.path?.endsWith('.json')) {
              return val;
            }
          }
        }
      }
    } catch (e) {
      console.warn('[leetie] Failed to extract from Monaco Editor', e);
    }
    return null;
  }

  static parseSubmissionResponse(rawResponse: any, codeOverride?: string): Submission | null {
    try {
      // Exclude "Run Code" interpret runs
      if (rawResponse?.interpret_id || rawResponse?.interpret_key || rawResponse?.data?.interpret_id || rawResponse?.data?.interpret_key) {
        return null;
      }

      // Shape 1 — GraphQL submissionCheck / submissionDetails / submissionResult / submitCode
      const gqlDetails =
        rawResponse?.data?.submissionCheck ||
        rawResponse?.data?.submissionDetails ||
        rawResponse?.data?.submissionResult ||
        rawResponse?.data?.submitCode ||
        (rawResponse?.data && typeof rawResponse.data === 'object' ? Object.values(rawResponse.data)[0] : null);

      // Shape 2 — Submission check polling endpoint / flat object
      const isCheckShape =
        !gqlDetails &&
        (rawResponse?.status_msg != null ||
          rawResponse?.state != null ||
          rawResponse?.statusDisplay != null);
      const checkDetails = isCheckShape ? rawResponse : null;

      const details = gqlDetails || checkDetails;
      if (!details) return null;

      if (details.interpret_id || details.interpret_key || (typeof details.task_id === 'string' && details.task_id.includes('interpret'))) {
        return null;
      }

      const status =
        details.statusDisplay ||
        details.status_display ||
        details.status_msg;
      if (status !== 'Accepted') return null;

      const slug = this.extractProblemSlug();
      const domMeta = this.extractFromDOM();

      const question = details.question || {};
      const problemId =
        question.questionFrontendId ||
        question.frontendQuestionId ||
        details.question_frontend_id ||
        details.frontend_question_id ||
        (domMeta.id !== '0' ? domMeta.id : null) ||
        question.questionId ||
        details.question_id ||
        '0';

      const problemTitle = question.title || domMeta.title || slug;
      const rawDiff = question.difficulty || details.difficulty || domMeta.difficulty;
      let difficulty: Difficulty = 'Easy';
      if (rawDiff) {
        const dStr = String(rawDiff).toLowerCase();
        if (dStr.includes('medium')) difficulty = 'Medium';
        else if (dStr.includes('hard')) difficulty = 'Hard';
        else if (dStr.includes('easy')) difficulty = 'Easy';
      }
      const topicTags = (question.topicTags || []).map((t: any) => t.name || t);

      // Code priority: (1) caller-provided override from MAIN world Monaco extraction,
      // (2) API response field, (3) direct Monaco extraction (only works in MAIN world).
      const code = codeOverride || details.code || this.extractFromMonaco() || '';
      if (!code) {
        console.warn('[leetie] Accepted submission detected, but solution code could not be retrieved.');
        return null;
      }

      // Extract submission ID with deterministic fallback to prevent duplicate commits
      const rawSubId =
        details._submission_id ||
        rawResponse?._submission_id ||
        details.submissionId ||
        details.id ||
        details.submission_id;

      const lang = details.lang || details.language || 'python3';
      const subIdStr = rawSubId
        ? String(rawSubId)
        : `${slug}_${lang}_${Math.floor(Date.now() / 10000)}`;

      if (subIdStr.includes('interpret_solution')) {
        return null;
      }

      return {
        submissionId: subIdStr,
        problem: {
          id: String(problemId),
          slug,
          title: problemTitle,
          difficulty,
          topicTags,
        },
        lang: details.lang || details.language || 'python3',
        code,
        runtime: details.runtime || details.status_runtime || 'N/A',
        memory: details.memory || details.status_memory || 'N/A',
        runtimePercentile: Math.round(
          details.runtimePercentile || details.runtime_percentile || 0
        ),
        memoryPercentile: Math.round(
          details.memoryPercentile || details.memory_percentile || 0
        ),
        timestamp: Date.now(),
      };
    } catch (err) {
      console.error('[leetie] Error parsing submission response', err);
      return null;
    }
  }
}
