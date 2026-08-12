import { DEFAULT_CONFIG, INITIAL_STATE, STORAGE_KEYS } from './constants';
import { CommitRecord, ExtensionConfig, ExtensionState } from './types';

// GitHub token prefixes to distinguish plaintext tokens from stale AES-GCM-encrypted
// values left by a previous build. chrome.storage.local is sandboxed to this extension,
// so plaintext storage is the correct and safe approach.
const GITHUB_TOKEN_PREFIXES = ['ghp_', 'gho_', 'github_pat_'];

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
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      const res = await chrome.storage.local.get(STORAGE_KEYS.STATE);
      return { ...INITIAL_STATE, ...(res[STORAGE_KEYS.STATE] || {}) };
    }
    const local = localStorage.getItem(STORAGE_KEYS.STATE);
    return local ? JSON.parse(local) : INITIAL_STATE;
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
    await this.setState({ recentCommits: updated, totalSynced: newTotal });
    return updated;
  },
};
