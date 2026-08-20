import { LANGUAGE_EXTENSION_MAP } from '../shared/constants';
import { storage } from '../shared/storage';
import { CommitRecord, Difficulty, ExtensionConfig, Submission } from '../shared/types';
import { GitHubService } from './github.service';

export class SyncService {
  private static BASE_URL = 'https://api.github.com';

  // Cache repos confirmed to exist this session to avoid redundant API calls
  // during history recovery (500 submissions = 500 ensureRepo calls without this).
  private static ensuredRepos = new Set<string>();

  private static getHeaders(token: string) {
    return {
      Authorization: `Bearer ${token}`,
      Accept: 'application/vnd.github.v3+json',
      'Content-Type': 'application/json',
      'User-Agent': 'leetie-extension',
    };
  }

  static toBase64(str: string): string {
    // TextEncoder correctly handles multi-byte Unicode (CJK, emoji, etc.)
    // btoa(unescape(encodeURIComponent(...))) is deprecated and unreliable.
    const bytes = new TextEncoder().encode(str || '');
    let binary = '';
    for (const byte of bytes) binary += String.fromCharCode(byte);
    return btoa(binary);
  }

  static getExtension(lang: string): string {
    const key = lang.toLowerCase().trim();
    return LANGUAGE_EXTENSION_MAP[key] || 'txt';
  }

  static formatFilePath(submission: Submission, config: ExtensionConfig, existingDifficulty?: Difficulty): string {
    const { problem, lang } = submission;
    const ext = this.getExtension(lang);
    const paddedId = String(problem.id).padStart(4, '0');
    const rawSubfolder = (config.solutionSubfolder || 'solutions').trim();
    const subfolder = rawSubfolder.replace(/^\/+|\/+$/g, '');

    const problemDir = `${paddedId}-${problem.slug}`;
    const fileName = `solution.${ext}`;
    const diff = existingDifficulty || problem.difficulty || 'Easy';

    if (!subfolder) {
      if (config.preferredDirFormat === 'flat') {
        return `${problemDir}/${fileName}`;
      }
      return `${diff}/${problemDir}/${fileName}`;
    }

    if (config.preferredDirFormat === 'flat') {
      return `${subfolder}/${problemDir}/${fileName}`;
    }

    return `${subfolder}/${diff}/${problemDir}/${fileName}`;
  }

  static getCommentStyle(lang: string): { start: string; end: string } {
    const ext = this.getExtension(lang);
    if (['html', 'xml'].includes(ext)) return { start: '<!--', end: ' -->' };
    if (['py', 'rb', 'sh', 'ex'].includes(ext)) return { start: '#', end: '' };
    if (['sql', 'hs', 'vhdl'].includes(ext)) return { start: '--', end: '' };
    if (['erl', 'rkt', 'clj'].includes(ext)) return { start: ';', end: '' };
    // JS, TS, Java, C, C++, Go, Rust, Kotlin, Swift, Dart, Scala, CS, etc.
    return { start: '//', end: '' };
  }

  static formatSolutionHeader(submission: Submission, githubUsername?: string): string {
    const { problem, lang, runtime, memory, runtimePercentile, memoryPercentile } = submission;
    const link = `https://leetcode.com/problems/${problem.slug}/`;
    const tags = problem.topicTags.length > 0 ? problem.topicTags.join(', ') : 'N/A';
    const { start: cs, end: ce } = this.getCommentStyle(lang);
    const authorName = (githubUsername || '').trim() || 'LeetCode User';
    const currentYear = new Date().getFullYear();

    return `${cs} ──────────────────────────────────────────────────${ce ? ' ' + ce : ''}
${cs} Problem  : ${problem.id}. ${problem.title}${ce ? ' ' + ce : ''}
${cs} Difficulty: ${problem.difficulty}${ce ? ' ' + ce : ''}
${cs} Tags     : ${tags}${ce ? ' ' + ce : ''}
${cs} Link     : ${link}${ce ? ' ' + ce : ''}
${cs} Runtime  : ${runtime} (beats ${runtimePercentile}%)${ce ? ' ' + ce : ''}
${cs} Memory   : ${memory} (beats ${memoryPercentile}%)${ce ? ' ' + ce : ''}
${cs} Language : ${lang}${ce ? ' ' + ce : ''}
${cs} Copyright: (c) ${currentYear} ${authorName}. All rights reserved.${ce ? ' ' + ce : ''}
${cs} Synced by: leetie${ce ? ' ' + ce : ''}
${cs} ──────────────────────────────────────────────────${ce ? ' ' + ce : ''}

${submission.code}`;
  }

  static async getFileSha(
    token: string,
    owner: string,
    repo: string,
    path: string,
    branch: string
  ): Promise<string | null> {
    try {
      const safePath = path.split('/').map((s) => encodeURIComponent(s)).join('/');
      const res = await fetch(`${this.BASE_URL}/repos/${owner}/${repo}/contents/${safePath}?ref=${encodeURIComponent(branch)}&_cb=${Date.now()}`, {
        headers: this.getHeaders(token),
        cache: 'no-store',
      });
      if (res.ok) {
        const data = await res.json();
        return data.sha || null;
      }
    } catch (err) {
      console.warn(`[leetie] Error checking SHA for ${path}:`, err);
    }
    return null;
  }

  static async putFile(
    token: string,
    owner: string,
    repo: string,
    path: string,
    content: string,
    commitMessage: string,
    branch: string,
    commitDate?: string,
    githubUsername?: string,
    retries = 3
  ): Promise<string> {
    for (let attempt = 1; attempt <= retries; attempt++) {
      try {
        const sha = await this.getFileSha(token, owner, repo, path, branch);
        const body: any = {
          message: commitMessage,
          content: this.toBase64(content),
          branch,
        };
        if (sha) body.sha = sha;

        if (commitDate) {
          const username = githubUsername || owner;
          const cleanEmail = `${username.toLowerCase().replace(/[^a-z0-9]/g, '')}@users.noreply.github.com`;
          const committerObj = {
            name: username,
            email: cleanEmail,
            date: commitDate,
          };
          body.author = committerObj;
          body.committer = committerObj;
        }

        const safePath = path.split('/').map((s) => encodeURIComponent(s)).join('/');
        const res = await fetch(`${this.BASE_URL}/repos/${owner}/${repo}/contents/${safePath}`, {
          method: 'PUT',
          headers: this.getHeaders(token),
          body: JSON.stringify(body),
        });

        if (!res.ok) {
          if (res.status === 409 && attempt < retries) {
            console.warn(`[leetie] GitHub API 409 Conflict on ${path}, retrying (${attempt}/${retries})...`);
            await new Promise((r) => setTimeout(r, 1200 * attempt));
            continue;
          }
          if (res.status === 429) {
            const retryAfter = parseInt(res.headers.get('Retry-After') || '60', 10);
            throw new Error(`GitHub API rate limited (429). Retry after ${retryAfter}s`);
          }
          const errorData = await res.json().catch(() => ({}));
          const errorMsg = errorData.message || errorData.error || `HTTP ${res.status}`;
          console.error(`[leetie] GitHub API PUT error (${res.status}) on ${path}:`, errorData);
          throw new Error(`GitHub API Error (${res.status}): ${errorMsg}`);
        }

        const resData = await res.json();
        return resData.content?.sha || resData.commit?.sha || 'success';
      } catch (err: any) {
        if (attempt === retries) throw err;
      }
    }
    throw new Error(`Failed to commit file ${path} after ${retries} attempts.`);
  }

  static generateReadmeContent(commits: CommitRecord[], _config: ExtensionConfig): string {
    const uniqueMap = new Map<string, CommitRecord>();
    for (const c of commits) {
      uniqueMap.set(c.problemSlug, c);
    }
    const uniqueCommits = Array.from(uniqueMap.values());

    uniqueCommits.sort((a, b) => {
      const idA = parseInt(a.problemTitle.match(/^(\d+)\./)?.[1] || '0', 10);
      const idB = parseInt(b.problemTitle.match(/^(\d+)\./)?.[1] || '0', 10);
      if (idA && idB) return idA - idB;
      return a.problemSlug.localeCompare(b.problemSlug);
    });

    const rows = uniqueCommits
      .map((c) => {
        const link = `https://leetcode.com/problems/${c.problemSlug}/`;
        const solutionLink = `./${c.githubPath}`;
        const cleanTitle = c.problemTitle
          .replace(/(?:Med\.|Medium|Easy|Hard|\s*-\s*BFS Solution|\s*-\s*DFS Solution)\s*$/i, '')
          .replace(/\|/g, '\\|')
          .trim();
        return `| ${c.problemSlug} | ${cleanTitle} | ${c.difficulty} | ${c.lang} | [Problem](${link}) | [Solution](${solutionLink}) |`;
      })
      .join('\n');

    return `# My LeetCode Solutions

> *Automatically synced by [leetie](https://github.com/leetie/leetie).*

## Progress Summary: ${uniqueCommits.length} Solved

| Slug | Problem | Difficulty | Language | Problem Link | Solution Code |
|------|---------|-----------|----------|--------------|---------------|
${rows}
`;
  }

  static async commitSubmission(submission: Submission, skipReadme = false): Promise<CommitRecord> {
    const config = await storage.getConfig();

    if (!config.githubToken || !config.githubUsername) {
      throw new Error('GitHub token or username not configured. Please open leetie options.');
    }

    const existingCommits = await storage.getCommits();
    const existingSameProblem = existingCommits.find(c => c.problemSlug === submission.problem.slug);
    const existingDiff = existingSameProblem?.difficulty as Difficulty | undefined;
    const filePath = this.formatFilePath(submission, config, existingDiff);

    // Ensure repo exists — cached per session to avoid redundant API calls during recovery
    const repoKey = `${config.githubUsername}/${config.repoName}`;
    if (!SyncService.ensuredRepos.has(repoKey)) {
      await GitHubService.ensureRepo(config.githubToken, config.githubUsername, config.repoName);
      SyncService.ensuredRepos.add(repoKey);
    }

    // Strict 3-Condition Deduplication Guard: Skip ONLY IF all 3 criteria are met simultaneously
    const now = Date.now();
    const hasSameSubmissionId = existingCommits.some((c) => c.submissionId === submission.submissionId);
    const hasSameProblemAndLang = existingCommits.some((c) => c.problemSlug === submission.problem.slug && c.lang === submission.lang);
    const hasRecentCommit = existingCommits.some(
      (c) => c.problemSlug === submission.problem.slug && c.lang === submission.lang && (now - c.committedAt) < 120000
    );

    if (hasSameSubmissionId && hasSameProblemAndLang && hasRecentCommit) {
      const existingRecord =
        existingCommits.find((c) => c.submissionId === submission.submissionId) ||
        existingCommits.find((c) => c.problemSlug === submission.problem.slug && c.lang === submission.lang);

      if (existingRecord) {
        // Verify whether the file still exists on GitHub (in case user deleted it on GitHub)
        const remoteSha = await this.getFileSha(
          config.githubToken,
          config.githubUsername,
          config.repoName,
          filePath,
          config.branch
        );
        if (remoteSha) {
          console.log('[leetie] All 3 duplicate criteria met simultaneously. Skipping commit:', submission.submissionId);
          return existingRecord;
        }
        console.log('[leetie] File missing on GitHub (deleted by user). Re-committing solution:', filePath);
      }
    }
    const fileContent = config.addHeaderComment ? this.formatSolutionHeader(submission, config.githubUsername) : submission.code;
    const commitMessage = `leetie: Add ${submission.problem.id}. ${submission.problem.title} [${submission.problem.difficulty}] (${submission.lang})`;

    let commitDate: string | undefined;
    if (submission.timestamp) {
      const ts = Number(submission.timestamp);
      const ms = ts < 10000000000 ? ts * 1000 : ts;
      const d = new Date(ms);
      if (!isNaN(d.getTime())) {
        commitDate = d.toISOString();
      }
    }

    const draftRecord: CommitRecord = {
      submissionId: submission.submissionId,
      problemSlug: submission.problem.slug,
      problemTitle: submission.problem.title,
      difficulty: submission.problem.difficulty,
      commitSha: 'pending',
      githubPath: filePath,
      committedAt: Date.now(),
      lang: submission.lang,
    };

    const filesToCommit: { path: string; content: string }[] = [
      { path: filePath, content: fileContent },
    ];

    if (config.autoReadme && !skipReadme) {
      const potentialCommits = [draftRecord, ...existingCommits.filter((c) => c.submissionId !== submission.submissionId)];
      const readmeContent = this.generateReadmeContent(potentialCommits, config);
      filesToCommit.push({ path: 'README.md', content: readmeContent });
    }

    // Atomic Single-Commit via GitHub Git Data API
    const sha = await GitHubService.commitFilesAtomic(
      config.githubToken,
      config.githubUsername,
      config.repoName,
      filesToCommit,
      commitMessage,
      config.branch,
      commitDate,
      config.githubUsername
    );

    const record: CommitRecord = {
      ...draftRecord,
      commitSha: sha,
    };

    // Save record to local storage
    await storage.addCommit(record);
    return record;
  }

  static async syncFromRepo(): Promise<{ count: number }> {
    const config = await storage.getConfig();
    if (!config.githubToken || !config.githubUsername) {
      throw new Error('GitHub token or username not configured.');
    }

    const tree = await GitHubService.fetchRepoTree(
      config.githubToken,
      config.githubUsername,
      config.repoName,
      config.branch
    );

    if (!tree.length) {
      return { count: 0 };
    }

    const extToLang: Record<string, string> = {
      py: 'python3',
      js: 'javascript',
      ts: 'typescript',
      cpp: 'cpp',
      c: 'c',
      java: 'java',
      cs: 'csharp',
      go: 'golang',
      rs: 'rust',
      rb: 'ruby',
      kt: 'kotlin',
      swift: 'swift',
      scala: 'scala',
      php: 'php',
      sql: 'mysql',
      dart: 'dart',
      ex: 'elixir',
      erl: 'erlang',
      rkt: 'racket',
      sh: 'bash',
    };

    const commitMap = new Map<string, CommitRecord>();
    const existingCommits = await storage.getCommits();
    const existingMap = new Map<string, CommitRecord>();
    for (const c of existingCommits) existingMap.set(c.problemSlug, c);

    for (const item of tree) {
      if (item.path.toLowerCase().endsWith('readme.md') || !item.path.includes('/')) continue;

      const parts = item.path.split('/');
      const filename = parts[parts.length - 1];
      const extMatch = filename.match(/\.([a-z0-9]+)$/i);
      if (!extMatch) continue;
      const ext = extMatch[1].toLowerCase();
      if (!extToLang[ext]) continue;

      // Strictly require a directory starting with digits (e.g., '0001-two-sum' or '0009-palindrome-number')
      const problemDir = parts.find((p) => /^\d+[-_]/.test(p));
      if (!problemDir) continue;

      const slugMatch = problemDir.match(/^(\d+)[-_](.+)$/);
      if (!slugMatch) continue;

      const problemNum = parseInt(slugMatch[1], 10);
      const slug = slugMatch[2];
      const title = `${problemNum}. ${slug.replace(/-/g, ' ').replace(/\b\w/g, (l) => l.toUpperCase())}`;

      let difficulty: Difficulty = existingMap.get(slug)?.difficulty || 'Easy';
      if (item.path.includes('/Easy/')) difficulty = 'Easy';
      else if (item.path.includes('/Medium/')) difficulty = 'Medium';
      else if (item.path.includes('/Hard/')) difficulty = 'Hard';

      const record: CommitRecord = {
        submissionId: `git_${slug}_${extToLang[ext]}`,
        problemSlug: slug,
        problemTitle: title,
        difficulty,
        commitSha: item.sha || 'git-synced',
        githubPath: item.path,
        committedAt: Date.now(),
        lang: extToLang[ext],
      };

      commitMap.set(slug, record);
    }

    const newRecords = Array.from(commitMap.values());
    await storage.setCommits(newRecords);
    return { count: newRecords.length };
  }
}
