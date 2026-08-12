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

    // Problem Title & ID from DOM title or heading
    let title = document.title.replace('- LeetCode', '').trim();
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

  static parseSubmissionResponse(graphqlResponse: any): Submission | null {
    try {
      const data = graphqlResponse?.data;
      const details = data?.submissionDetails || data?.submissionResult;

      if (!details) return null;

      const status = details.statusDisplay || details.status_display;
      if (status !== 'Accepted') return null;

      const slug = this.extractProblemSlug();
      const domMeta = this.extractFromDOM();

      const question = details.question || {};
      const problemId = question.questionId || domMeta.id || '0';
      const problemTitle = question.title || domMeta.title || slug;
      const difficulty: Difficulty = (question.difficulty as Difficulty) || domMeta.difficulty || 'Easy';
      const topicTags = (question.topicTags || []).map((t: any) => t.name || t);

      const code = details.code || this.extractFromMonaco() || '';

      if (!code) {
        console.warn('[leetie] Accepted submission detected, but solution code could not be retrieved.');
        return null;
      }

      return {
        submissionId: String(details.submissionId || details.id || Date.now()),
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
        runtimePercentile: Math.round(details.runtimePercentile || details.runtime_percentile || 0),
        memoryPercentile: Math.round(details.memoryPercentile || details.memory_percentile || 0),
        timestamp: Date.now(),
      };
    } catch (err) {
      console.error('[leetie] Error parsing submission GraphQL response', err);
      return null;
    }
  }
}
