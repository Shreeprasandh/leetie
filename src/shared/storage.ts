import { DEFAULT_CONFIG, INITIAL_STATE, STORAGE_KEYS } from './constants';
import { CommitRecord, ExtensionConfig, ExtensionState } from './types';

export const storage = {
  async getConfig(): Promise<ExtensionConfig> {
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      const res = await chrome.storage.local.get(STORAGE_KEYS.CONFIG);
      return { ...DEFAULT_CONFIG, ...(res[STORAGE_KEYS.CONFIG] || {}) };
    }
    const local = localStorage.getItem(STORAGE_KEYS.CONFIG);
    return local ? JSON.parse(local) : DEFAULT_CONFIG;
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
    const current = await this.getState();
    const updated = { ...current, ...state };
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      await chrome.storage.local.set({ [STORAGE_KEYS.STATE]: updated });
    } else {
      localStorage.setItem(STORAGE_KEYS.STATE, JSON.stringify(updated));
    }
    return updated;
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
    const updated = [record, ...commits].slice(0, 100);
    if (typeof chrome !== 'undefined' && chrome.storage?.local) {
      await chrome.storage.local.set({ [STORAGE_KEYS.COMMITS]: updated });
    } else {
      localStorage.setItem(STORAGE_KEYS.COMMITS, JSON.stringify(updated));
    }
    await this.setState({ recentCommits: updated, totalSynced: updated.length });
    return updated;
  },
};
