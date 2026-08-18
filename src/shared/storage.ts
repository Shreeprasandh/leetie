import { DEFAULT_CONFIG, INITIAL_STATE, STORAGE_KEYS } from './constants';
import { CommitRecord, Difficulty, ExtensionConfig, ExtensionState, Submission, SyncedStats } from './types';

function toLocalDateStr(dInput: number | Date): string {
  const d = typeof dInput === 'number' ? new Date(dInput) : dInput;
  if (isNaN(d.getTime())) return '';
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

export function computeSyncedStats(commits: CommitRecord[]): SyncedStats {
  const uniqueProblems = new Map<string, Difficulty>();
  for (const c of commits) {
    if (c.problemSlug && !uniqueProblems.has(c.problemSlug)) {
      uniqueProblems.set(c.problemSlug, c.difficulty || 'Easy');
    }
  }

  let easySolved = 0;
  let mediumSolved = 0;
  let hardSolved = 0;

  uniqueProblems.forEach((diff) => {
    const norm = String(diff || '').toLowerCase().trim();
    if (norm === 'easy') easySolved++;
    else if (norm === 'medium') mediumSolved++;
    else if (norm === 'hard') hardSolved++;
    else easySolved++;
  });

  const totalSolved = uniqueProblems.size;
  const totalSubmissions = commits.length;
  const acceptanceRate = totalSubmissions > 0 ? Math.round((totalSolved / totalSubmissions) * 1000) / 10 : 0;

  // Streak calculation based on distinct local calendar commit dates
  const commitDays = Array.from(
    new Set(
      commits
        .map((c) => (c.committedAt ? toLocalDateStr(c.committedAt) : ''))
        .filter(Boolean)
    )
  ).sort().reverse();

  let streak = 0;
  if (commitDays.length > 0) {
    const todayStr = toLocalDateStr(new Date());
    const yesterdayDate = new Date();
    yesterdayDate.setDate(yesterdayDate.getDate() - 1);
    const yesterdayStr = toLocalDateStr(yesterdayDate);

    let currentCheckDate =
      commitDays[0] === todayStr || commitDays[0] === yesterdayStr ? new Date(commitDays[0] + 'T00:00:00') : null;

    if (currentCheckDate) {
      for (const dayStr of commitDays) {
        const expectedStr = toLocalDateStr(currentCheckDate);
        if (dayStr === expectedStr) {
          streak++;
          currentCheckDate.setDate(currentCheckDate.getDate() - 1);
        } else {
          break;
        }
      }
    }
  }

  return {
    easySolved,
    mediumSolved,
    hardSolved,
    totalSolved,
    totalSubmissions,
    acceptanceRate,
    streak,
  };
}

// GitHub token prefixes to distinguish plaintext tokens from stale AES-GCM-encrypted
// values left by a previous build. chrome.storage.local is sandboxed to this extension,
// so plaintext storage is the correct and safe approach.
const GITHUB_TOKEN_PREFIXES = ['ghp_', 'gho_', 'ghu_', 'ghr_', 'ghs_', 'github_pat_'];

function sanitizeToken(token: string): string {
  if (!token) return '';
  if (GITHUB_TOKEN_PREFIXES.some((p) => token.startsWith(p))) return token;
  // Stale encrypted blob from a previous version — clear it so the user is prompted to reconnect.
  console.warn('[leetie] Stale encrypted token cleared. Please reconnect your GitHub account.');
  return '';
}

export const storage = {
  async getConfig(): Promise<ExtensionConfig> {
    let rawConfig: ExtensionConfig;
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      const res = await chrome.storage.local.get(STORAGE_KEYS.CONFIG);
      rawConfig = { ...DEFAULT_CONFIG, ...(res[STORAGE_KEYS.CONFIG] || {}) };
    } else {
      const local = localStorage.getItem(STORAGE_KEYS.CONFIG);
      rawConfig = local ? JSON.parse(local) : DEFAULT_CONFIG;
    }
    // Migrate: clear any stale encrypted token from previous versions
    rawConfig.githubToken = sanitizeToken(rawConfig.githubToken);
    return rawConfig;
  },

  async setConfig(config: Partial<ExtensionConfig>): Promise<ExtensionConfig> {
    const current = await this.getConfig();
    const updated = { ...current, ...config };
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      await chrome.storage.local.set({ [STORAGE_KEYS.CONFIG]: updated });
    } else {
      localStorage.setItem(STORAGE_KEYS.CONFIG, JSON.stringify(updated));
    }
    return updated;
  },

  async getState(): Promise<ExtensionState> {
    const commits = await this.getCommits();
    const stats = computeSyncedStats(commits);
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      const res = await chrome.storage.local.get(STORAGE_KEYS.STATE);
      const stored = res[STORAGE_KEYS.STATE] || {};
      return { ...INITIAL_STATE, ...stored, syncedStats: stats };
    }
    const local = localStorage.getItem(STORAGE_KEYS.STATE);
    const parsed = local ? JSON.parse(local) : {};
    return { ...INITIAL_STATE, ...parsed, syncedStats: stats };
  },

  async setState(state: Partial<ExtensionState>): Promise<ExtensionState> {
    let current: ExtensionState;
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      const res = await chrome.storage.local.get(STORAGE_KEYS.STATE);
      current = { ...INITIAL_STATE, ...(res[STORAGE_KEYS.STATE] || {}) };
      const updated = { ...current, ...state };
      await chrome.storage.local.set({ [STORAGE_KEYS.STATE]: updated });
      return updated;
    } else {
      const local = localStorage.getItem(STORAGE_KEYS.STATE);
      current = local ? JSON.parse(local) : INITIAL_STATE;
      const updated = { ...current, ...state };
      localStorage.setItem(STORAGE_KEYS.STATE, JSON.stringify(updated));
      return updated;
    }
  },

  async getCommits(): Promise<CommitRecord[]> {
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      const res = await chrome.storage.local.get(STORAGE_KEYS.COMMITS);
      return res[STORAGE_KEYS.COMMITS] || [];
    }
    const local = localStorage.getItem(STORAGE_KEYS.COMMITS);
    return local ? JSON.parse(local) : [];
  },

  async addCommit(record: CommitRecord): Promise<CommitRecord[]> {
    const commits = await this.getCommits();

    // Dedup: if this submissionId is already recorded, skip silently.
    // Prevents duplicate entries when both the check endpoint and the
    // GraphQL submissionDetails query fire for the same accepted submission.
    if (commits.some((c) => c.submissionId === record.submissionId)) {
      return commits;
    }

    const updated = [record, ...commits].slice(0, 100);
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      await chrome.storage.local.set({ [STORAGE_KEYS.COMMITS]: updated });
    } else {
      localStorage.setItem(STORAGE_KEYS.COMMITS, JSON.stringify(updated));
    }

    // totalSynced is a running counter, not list.length.
    // The list is capped at 100 but the user may have synced many more.
    const currentState = await this.getState();
    const newTotal = (currentState.totalSynced || 0) + 1;
    const stats = computeSyncedStats(updated);
    await this.setState({ recentCommits: updated, totalSynced: newTotal, syncedStats: stats });
    return updated;
  },

  async setCommits(commits: CommitRecord[]): Promise<CommitRecord[]> {
    const updated = commits.slice(0, 100);
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      await chrome.storage.local.set({ [STORAGE_KEYS.COMMITS]: updated });
    } else {
      localStorage.setItem(STORAGE_KEYS.COMMITS, JSON.stringify(updated));
    }
    const stats = computeSyncedStats(updated);
    await this.setState({
      syncStatus: 'idle',
      recentCommits: updated,
      totalSynced: stats.totalSolved,
      syncedStats: stats,
      lastError: null,
    });
    return updated;
  },

  async getPendingSubmissions(): Promise<Submission[]> {
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      const res = await chrome.storage.local.get(STORAGE_KEYS.PENDING_SUBMISSIONS);
      return res[STORAGE_KEYS.PENDING_SUBMISSIONS] || [];
    }
    const local = localStorage.getItem(STORAGE_KEYS.PENDING_SUBMISSIONS);
    return local ? JSON.parse(local) : [];
  },

  async addPendingSubmission(submission: Submission): Promise<Submission[]> {
    const current = await this.getPendingSubmissions();
    if (current.some((s) => s.submissionId === submission.submissionId)) return current;
    const updated = [...current, submission];
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      await chrome.storage.local.set({ [STORAGE_KEYS.PENDING_SUBMISSIONS]: updated });
    } else {
      localStorage.setItem(STORAGE_KEYS.PENDING_SUBMISSIONS, JSON.stringify(updated));
    }
    return updated;
  },

  async removePendingSubmission(submissionId: string): Promise<Submission[]> {
    const current = await this.getPendingSubmissions();
    const updated = current.filter((s) => s.submissionId !== submissionId);
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      await chrome.storage.local.set({ [STORAGE_KEYS.PENDING_SUBMISSIONS]: updated });
    } else {
      localStorage.setItem(STORAGE_KEYS.PENDING_SUBMISSIONS, JSON.stringify(updated));
    }
    return updated;
  },
};
