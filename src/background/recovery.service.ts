import { storage } from '../shared/storage';
import { Difficulty, Submission } from '../shared/types';
import { SyncService } from './sync.service';

export interface RecoveryProgress {
  current: number;
  total: number;
}

export class RecoveryService {
  private static isRunning = false;

  static stopRecovery() {
    this.isRunning = false;
  }

  // ---------------------------------------------------------------------------
  // fetchGraphQL
  // Routes all LeetCode GraphQL requests through the active content script tab.
  // Reason: MV3 service workers do NOT share the browser cookie jar, so
  // fetch(..., { credentials: 'include' }) from a SW returns unauthenticated
  // responses. The content script runs in a real page context and has cookies.
  // ---------------------------------------------------------------------------
  private static async fetchGraphQL(requestBody: Record<string, unknown>): Promise<any> {
    if (typeof chrome === 'undefined' || !chrome.tabs) {
      throw new Error('Chrome tabs API not available.');
    }

    // Target any tab on leetcode.com where content script is active
    const tabs = await chrome.tabs.query({ url: 'https://leetcode.com/*' });
    if (!tabs.length) {
      throw new Error(
        'No active LeetCode tab found. Please open LeetCode in your browser and try again.'
      );
    }

    let lastErr: Error | null = null;
    for (const tab of tabs) {
      if (tab.id == null) continue;

      const trySendMessage = (): Promise<any> =>
        new Promise<any>((resolve, reject) => {
          chrome.tabs.sendMessage(
            tab.id!,
            { type: 'PROXY_GRAPHQL', payload: { body: JSON.stringify(requestBody) } },
            (response) => {
              if (chrome.runtime.lastError) {
                return reject(new Error(chrome.runtime.lastError.message));
              }
              if (!response?.success) {
                return reject(new Error(response?.error || 'GraphQL proxy request failed.'));
              }
              resolve(response.data);
            }
          );
        });

      try {
        const res = await trySendMessage();
        return res;
      } catch (err: any) {
        lastErr = err;
        // If content script is disconnected (e.g. extension reloaded while tab was open), re-inject content script dynamically
        if (
          err.message &&
          (err.message.includes('Could not establish connection') || err.message.includes('Receiving end does not exist'))
        ) {
          if (chrome.scripting) {
            try {
              await chrome.scripting.executeScript({
                target: { tabId: tab.id },
                files: ['content/index.js'],
              });
              // Brief delay for listener registration
              await new Promise((r) => setTimeout(r, 150));
              const retryRes = await trySendMessage();
              return retryRes;
            } catch (injErr: any) {
              console.warn('[leetie] Dynamic content script injection failed:', injErr);
            }
          }
        }
      }
    }

    throw new Error(`LeetCode tab unreachable: ${lastErr?.message || 'Please refresh your LeetCode page.'}`);
  }

  static async fetchSubmissionList(offset: number, limit = 20): Promise<{ submissions: any[]; hasNext: boolean }> {
    const query = `
      query submissionList($offset: Int!, $limit: Int!) {
        submissionList(offset: $offset, limit: $limit) {
          hasNext
          submissions {
            id
            statusDisplay
            lang
            timestamp
            titleSlug
          }
        }
      }
    `;

    const data = await this.fetchGraphQL({
      operationName: 'submissionList',
      query,
      variables: { offset, limit },
    });

    const list = data?.data?.submissionList;
    return {
      submissions: list?.submissions || [],
      hasNext: Boolean(list?.hasNext),
    };
  }

  static async fetchSubmissionDetail(id: number, rawTimestamp?: number): Promise<Submission | null> {
    const query = `
      query submissionDetails($submissionId: Int!) {
        submissionDetails(submissionId: $submissionId) {
          statusDisplay
          lang
          code
          runtime
          memory
          runtimePercentile
          memoryPercentile
          timestamp
          question {
            questionId
            title
            titleSlug
            difficulty
            topicTags {
              name
            }
          }
        }
      }
    `;

    let data: any;
    try {
      data = await this.fetchGraphQL({
        operationName: 'submissionDetails',
        query,
        variables: { submissionId: Number(id) },
      });
    } catch (err: any) {
      console.warn(`[leetie] fetchSubmissionDetail proxy error for id ${id}:`, err);
      throw err;
    }

    if (data?.errors && data.errors.length > 0) {
      throw new Error(`LeetCode GraphQL error: ${data.errors[0].message || 'Unknown GraphQL error'}`);
    }

    const details = data?.data?.submissionDetails;

    if (!details) {
      console.warn(`[leetie] fetchSubmissionDetail returned null details for id ${id}:`, data);
      throw new Error(`LeetCode returned null details for submission #${id}`);
    }

    if (details.statusDisplay !== 'Accepted') {
      return null;
    }

    const q = details.question || {};
    const unixTs = Number(details.timestamp || rawTimestamp || 0);
    const problemId = String(q.questionFrontendId || q.questionId || '0');
    return {
      submissionId: String(id),
      problem: {
        id: problemId,
        slug: q.titleSlug || 'problem',
        title: q.title || 'Problem',
        difficulty: (q.difficulty as Difficulty) || 'Easy',
        topicTags: (q.topicTags || []).map((t: any) => t.name || t),
      },
      lang: details.lang || 'python3',
      code: details.code || '',
      runtime: details.runtime || 'N/A',
      memory: details.memory || 'N/A',
      runtimePercentile: Math.round(details.runtimePercentile || 0),
      memoryPercentile: Math.round(details.memoryPercentile || 0),
      timestamp: unixTs > 0 ? unixTs * 1000 : Date.now(),
    };
  }

  static deduplicateSubmissions(submissions: any[]): any[] {
    const map = new Map<string, any>();
    for (const sub of submissions) {
      if (sub.statusDisplay === 'Accepted') {
        const key = `${sub.titleSlug}_${sub.lang}`;
        if (!map.has(key)) {
          map.set(key, sub);
        }
      }
    }
    return Array.from(map.values());
  }

  static async startRecovery(): Promise<void> {
    if (this.isRunning) return;
    this.isRunning = true;

    try {
      const config = await storage.getConfig();
      if (!config.githubToken || !config.githubUsername) {
        const errMsg = 'GitHub Token or Username is missing. Please connect your GitHub account first.';
        await storage.setState({ syncStatus: 'error', lastError: errMsg, recoveryProgress: null });
        this.isRunning = false;
        return;
      }

      // Show the recovery card immediately with an indeterminate state
      await storage.setState({
        syncStatus: 'recovering',
        lastError: null,
        recoveryProgress: { current: 0, total: 0 },
      });

      // Step 1: Paginate to collect all submission metadata
      const allSubmissions: any[] = [];
      let offset = 0;
      const limit = 20;

      while (this.isRunning) {
        let page: { submissions: any[]; hasNext: boolean };
        try {
          page = await this.fetchSubmissionList(offset, limit);
        } catch (err: any) {
          // Most likely cause: user closed the LeetCode problem tab.
          const msg = `Recovery paused: ${err.message}. Re-open a LeetCode problem page and try again.`;
          await storage.setState({ syncStatus: 'error', lastError: msg, recoveryProgress: null });
          this.isRunning = false;
          return;
        }
        allSubmissions.push(...page.submissions);
        // Note: progress bar is only updated during the commit phase (Step 3)
        // to give a meaningful indicator. Showing progress here is misleading
        // because current ≈ total at all times during pagination.
        if (!page.hasNext) break;
        offset += limit;
        await new Promise((r) => setTimeout(r, 300));
      }

      if (!this.isRunning) {
        await storage.setState({ syncStatus: 'idle', recoveryProgress: null });
        return;
      }

      // Step 2: Deduplicate (one per problem + language)
      const uniqueSubmissions = this.deduplicateSubmissions(allSubmissions);
      const total = uniqueSubmissions.length;
      let successCount = 0;

      // Step 3: Fetch detail and commit
      for (let i = 0; i < total; i++) {
        if (!this.isRunning) break;

        const subMeta = uniqueSubmissions[i];
        await storage.setState({
          recoveryProgress: { current: i + 1, total },
        });

        try {
          const fullSubmission = await this.fetchSubmissionDetail(subMeta.id, subMeta.timestamp);
          if (fullSubmission) {
            // Pass skipReadme = true to avoid rate-limiting GitHub with individual README commits
            await SyncService.commitSubmission(fullSubmission, true);
            successCount++;
          }
        } catch (err: any) {
          console.warn(`[leetie] Failed to commit item ${subMeta.id}:`, err);
          // Handle both LeetCode and GitHub rate limits
          if (err.message && (err.message.includes('429') || err.message.includes('rate limited'))) {
            const match = err.message.match(/Retry after (\d+)s/);
            const waitMs = match ? parseInt(match[1]) * 1000 : 60000;
            console.warn(`[leetie] Rate limited. Backing off for ${waitMs / 1000}s...`);
            await new Promise((r) => setTimeout(r, waitMs));
          } else {
            await storage.setState({ lastError: `Commit error on #${subMeta.titleSlug}: ${err.message}` });
          }
        }

        // 1000ms polite delay to safeguard LeetCode & GitHub APIs
        await new Promise((r) => setTimeout(r, 1000));
      }

      // Single consolidated README update at the end of recovery if autoReadme is enabled
      if (config.autoReadme && successCount > 0) {
        try {
          const allCommits = await storage.getCommits();
          const readmeContent = SyncService.generateReadmeContent(allCommits, config);
          await SyncService.putFile(
            config.githubToken,
            config.githubUsername,
            config.repoName,
            'README.md',
            readmeContent,
            'leetie: Bulk history recovery README update',
            config.branch
          );
        } catch (rErr) {
          console.warn('[leetie] Consolidated recovery README update failed:', rErr);
        }
      }

      await storage.setState({
        syncStatus: 'idle',
        recoveryProgress: null,
        lastError: successCount === 0 && total > 0 ? 'Recovery completed but 0 commits succeeded. Please check GitHub permissions.' : null,
      });
    } catch (err: any) {
      console.error('[leetie] History recovery error:', err);
      await storage.setState({ syncStatus: 'error', lastError: err.message, recoveryProgress: null });
    } finally {
      this.isRunning = false;
    }
  }
}
