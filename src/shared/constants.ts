import { ExtensionConfig, ExtensionState } from './types';

export const DEFAULT_CONFIG: ExtensionConfig = {
  githubToken: '',
  githubUsername: 'Shreeprasandh',
  repoName: 'leetie',
  branch: 'main',
  solutionSubfolder: 'solutions',
  addHeaderComment: true,
  autoReadme: true,
  preferredDirFormat: 'difficulty-first',
  proxyUrl: 'https://leetie-proxy.vercel.app/api/exchange',
};

export const INITIAL_STATE: ExtensionState = {
  isAuthenticated: false,
  syncStatus: 'idle',
  totalSynced: 0,
  recentCommits: [],
  lastError: null,
  recoveryProgress: null,
};

export const STORAGE_KEYS = {
  TOKEN: 'leetie_token',
  USERNAME: 'leetie_username',
  CONFIG: 'leetie_config',
  STATE: 'leetie_state',
  COMMITS: 'leetie_commits',
  SYNCED_SLUGS: 'leetie_synced_slugs',
} as const;

export const LANGUAGE_EXTENSION_MAP: Record<string, string> = {
  python: 'py',
  python3: 'py',
  javascript: 'js',
  typescript: 'ts',
  java: 'java',
  cpp: 'cpp',
  c: 'c',
  csharp: 'cs',
  golang: 'go',
  go: 'go',
  rust: 'rs',
  kotlin: 'kt',
  swift: 'swift',
  ruby: 'rb',
  scala: 'scala',
  php: 'php',
  sql: 'sql',
};
