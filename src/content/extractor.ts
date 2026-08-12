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
    }

    // Difficulty extraction from page DOM elements
    let difficulty: Difficulty = 'Easy';
    const diffElement = document.querySelector(
      '[class*="text-difficulty-"], [class*="text-sd-easy"], [class*="text-sd-medium"], [class*="text-sd-hard"]'
    );
    if (diffElement) {
      const text = diffElement.textContent?.trim();
      if (text === 'Medium') difficulty = 'Medium';
      if (text === 'Hard') difficulty = 'Hard';
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
      // Access Monaco Editor instance if available on page
      const win = window as any;
      if (win.monaco?.editor) {
        const models = win.monaco.editor.getModels();
        if (models && models.length > 0) {
          return models[0].getValue();
        }
      }
    } catch (e) {
      console.warn('[leetie] Failed to extract from Monaco Editor', e);
    }
    return null;
  }

  static parseSubmissionResponse(rawResponse: any, codeOverride?: string): Submission | null {
    try {
      // --- Shape 1: GraphQL submissionDetails / submissionResult ---
      // Fired when the user navigates to a submission detail page.
      // { data: { submissionDetails: { statusDisplay, lang, code, question: {...} } } }
      const gqlDetails =
        rawResponse?.data?.submissionDetails || rawResponse?.data?.submissionResult;

      // --- Shape 2: Submission check polling endpoint ---
      // Fired repeatedly by LeetCode until the judge returns a verdict.
      // Flat object: { state, status_msg, lang, submission_id, runtime, memory, ... }
      // This is the primary trigger in LeetCode's current submission flow.
      const isCheckShape =
        !gqlDetails &&
        (rawResponse?.status_msg != null || rawResponse?.state != null);
      const checkDetails = isCheckShape ? rawResponse : null;

      const details = gqlDetails || checkDetails;
      if (!details) return null;

      const status =
        details.statusDisplay ||
        details.status_display ||
        details.status_msg;
      if (status !== 'Accepted') return null;

      const slug = this.extractProblemSlug();
      const domMeta = this.extractFromDOM();

      const question = details.question || {};
      const problemId =
        question.questionId ||
        details.question_id ||
        domMeta.id ||
        '0';
      const problemTitle = question.title || domMeta.title || slug;
      const difficulty: Difficulty =
        (question.difficulty as Difficulty) || domMeta.difficulty || 'Easy';
      const topicTags = (question.topicTags || []).map((t: any) => t.name || t);

      // Code priority: (1) caller-provided override from MAIN world Monaco extraction,
      // (2) API response field, (3) direct Monaco extraction (only works in MAIN world).
      const code = codeOverride || details.code || this.extractFromMonaco() || '';
      if (!code) {
        console.warn('[leetie] Accepted submission detected, but solution code could not be retrieved.');
        return null;
      }

      // Extract genuine LeetCode submission ID — filter out test runs ("Run Code")
      const rawSubId = details.submissionId || details.id || details.submission_id;
      if (!rawSubId) {
        console.warn('[leetie] Accepted response ignored — missing genuine LeetCode submission_id.');
        return null;
      }

      const subIdStr = String(rawSubId);
      if (subIdStr.startsWith('runcode_') || subIdStr.startsWith('interpret_')) {
        console.warn('[leetie] Sample test run ignored:', subIdStr);
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
