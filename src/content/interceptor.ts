import { LeetCodeExtractor } from './extractor';
import { Submission } from '../shared/types';

export function setupFetchInterceptor(onAccepted: (submission: Submission) => void) {
  const originalFetch = window.fetch.bind(window);

  window.fetch = async function (...args) {
    const response = await originalFetch(...args);

    try {
      const rawUrl = args[0];
      const url =
        typeof rawUrl === 'string'
          ? rawUrl
          : rawUrl instanceof URL
          ? rawUrl.href
          : (rawUrl as Request)?.url || '';
      
      if (url.includes('/graphql')) {
        const clone = response.clone();
        clone
          .json()
          .then((data) => {
            const submission = LeetCodeExtractor.parseSubmissionResponse(data);
            if (submission) {
              console.log('[leetie] Accepted submission intercepted:', submission.problem.title);
              onAccepted(submission);
            }
          })
          .catch(() => {
            // Ignore non-JSON GraphQL responses
          });
      }
    } catch (e) {
      // Silently continue — never interrupt LeetCode core functionality
    }

    return response;
  };
}
