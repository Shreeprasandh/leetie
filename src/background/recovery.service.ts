import { storage } from '../shared/storage';
import { Difficulty, Submission } from '../shared/types';
import { SyncService } from './sync.service';

export interface RecoveryProgress {
  current: number;
  total: number;
  currentSlug: string;
  isPaused: boolean;
}

export class RecoveryService {
  private static LEETCODE_GRAPHQL = 'https://leetcode.com/graphql';
  private static isRunning = false;

  static stopRecovery() {
    this.isRunning = false;
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

    const res = await fetch(this.LEETCODE_GRAPHQL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        query,
        variables: { offset, limit },
      }),
    });

    if (!res.ok) {
      throw new Error(`LeetCode API request failed: ${res.statusText}`);
    }

    const data = await res.json();
    const list = data?.data?.submissionList;
    return {
      submissions: list?.submissions || [],
      hasNext: Boolean(list?.hasNext),
    };
  }

  static async fetchSubmissionDetail(id: number): Promise<Submission | null> {
    const query = `
      query submissionDetails($id: Int!) {
        submissionDetails(submissionId: $id) {
          statusDisplay
          lang
          code
          runtime
          memory
          runtimePercentile
          memoryPercentile
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

    const res = await fetch(this.LEETCODE_GRAPHQL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        query,
        variables: { id: Number(id) },
      }),
    });

    if (!res.ok) return null;

    const data = await res.json();
    const details = data?.data?.submissionDetails;

    if (!details || details.statusDisplay !== 'Accepted') return null;

    const q = details.question || {};
    return {
      submissionId: String(id),
      problem: {
        id: String(q.questionId || '0'),
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
      timestamp: Date.now(),
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
      await storage.setState({ syncStatus: 'recovering', lastError: null });

      // Step 1: Paginate to collect all submission metadata
      const allSubmissions: any[] = [];
      let offset = 0;
      const limit = 20;

      while (this.isRunning) {
        const page = await this.fetchSubmissionList(offset, limit);
        allSubmissions.push(...page.submissions);

        await storage.setState({
          recoveryProgress: { current: allSubmissions.length, total: allSubmissions.length + (page.hasNext ? limit : 0) },
        });

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

      // Step 3: Fetch detail and commit
      for (let i = 0; i < total; i++) {
        if (!this.isRunning) break;

        const subMeta = uniqueSubmissions[i];
        await storage.setState({
          recoveryProgress: { current: i + 1, total },
        });

        try {
          const fullSubmission = await this.fetchSubmissionDetail(subMeta.id);
          if (fullSubmission) {
            await SyncService.commitSubmission(fullSubmission);
          }
        } catch (err: any) {
          if (err.message && err.message.includes('429')) {
            console.warn('[leetie] Rate limit encountered. Backing off for 5 seconds...');
            await new Promise((r) => setTimeout(r, 5000));
          } else {
            console.warn(`[leetie] Failed to commit item ${subMeta.id}:`, err);
          }
        }

        // 1000ms polite delay to safeguard LeetCode & GitHub APIs
        await new Promise((r) => setTimeout(r, 1000));
      }

      await storage.setState({ syncStatus: 'idle', recoveryProgress: null });
    } catch (err: any) {
      console.error('[leetie] History recovery error:', err);
      await storage.setState({ syncStatus: 'error', lastError: err.message, recoveryProgress: null });
    } finally {
      this.isRunning = false;
    }
  }
}
