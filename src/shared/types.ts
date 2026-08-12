export type Difficulty = 'Easy' | 'Medium' | 'Hard';

export type SyncStatus = 'idle' | 'syncing' | 'recovering' | 'error';

export interface Problem {
  id: string;
  slug: string;
  title: string;
  difficulty: Difficulty;
  topicTags: string[];
}

export interface Submission {
  submissionId: string;
  problem: Problem;
  lang: string;
  code: string;
  runtime: string;
  memory: string;
  runtimePercentile: number;
  memoryPercentile: number;
  timestamp: number;
}

export interface CommitRecord {
  submissionId: string;
  problemSlug: string;
  problemTitle: string;
  difficulty: Difficulty;
  commitSha: string;
  githubPath: string;
  committedAt: number;
  lang: string;
}

export interface ExtensionConfig {
  githubToken: string;
  githubUsername: string;
  repoName: string;
  branch: string;
  solutionSubfolder: string;
  addHeaderComment: boolean;
  autoReadme: boolean;
  preferredDirFormat: 'difficulty-first' | 'flat';
  proxyUrl: string;
}

export interface ExtensionState {
  isAuthenticated: boolean;
  syncStatus: SyncStatus;
  totalSynced: number;
  recentCommits: CommitRecord[];
  lastError: string | null;
  recoveryProgress: { current: number; total: number } | null;
}
