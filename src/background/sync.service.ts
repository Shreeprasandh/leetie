import { LANGUAGE_EXTENSION_MAP } from '../shared/constants';
import { storage } from '../shared/storage';
import { CommitRecord, ExtensionConfig, Submission } from '../shared/types';
import { GitHubService } from './github.service';

export class SyncService {
  private static BASE_URL = 'https://api.github.com';

  private static getHeaders(token: string) {
    return {
      Authorization: `Bearer ${token}`,
      Accept: 'application/vnd.github.v3+json',
      'Content-Type': 'application/json',
      'User-Agent': 'leetie-extension',
    };
  }

  static toBase64(str: string): string {
    return btoa(unescape(encodeURIComponent(str)));
  }

  static getExtension(lang: string): string {
    const key = lang.toLowerCase().trim();
    return LANGUAGE_EXTENSION_MAP[key] || 'txt';
  }

  static formatFilePath(submission: Submission, config: ExtensionConfig): string {
    const { problem, lang } = submission;
    const ext = this.getExtension(lang);
    const paddedId = String(problem.id).padStart(4, '0');
    const subfolder = config.solutionSubfolder || 'solutions';

    if (config.preferredDirFormat === 'flat') {
      return `${subfolder}/${paddedId}-${problem.slug}/solution.${ext}`;
    }

    // Default: difficulty-first
    return `${subfolder}/${problem.difficulty}/${paddedId}-${problem.slug}/solution.${ext}`;
  }

  static formatSolutionHeader(submission: Submission): string {
    const { problem, lang, runtime, memory, runtimePercentile, memoryPercentile } = submission;
    const link = `https://leetcode.com/problems/${problem.slug}/`;
    const tags = problem.topicTags.length > 0 ? problem.topicTags.join(', ') : 'N/A';

    const commentChar = ['html', 'xml'].includes(this.getExtension(lang)) ? '<!--' : '#';
    const commentEnd = ['html', 'xml'].includes(this.getExtension(lang)) ? '-->' : '';

    return `${commentChar} ──────────────────────────────────────────────────
${commentChar} Problem  : ${problem.id}. ${problem.title}
${commentChar} Difficulty: ${problem.difficulty}
${commentChar} Tags     : ${tags}
${commentChar} Link     : ${link}
${commentChar} Runtime  : ${runtime} (beats ${runtimePercentile}%)
${commentChar} Memory   : ${memory} (beats ${memoryPercentile}%)
${commentChar} Language : ${lang}
${commentChar} Synced by: leetie
${commentChar} ────────────────────────────────────────────────── ${commentEnd}

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
      const res = await fetch(`${this.BASE_URL}/repos/${owner}/${repo}/contents/${path}?ref=${branch}`, {
        headers: this.getHeaders(token),
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
    githubUsername?: string
  ): Promise<string> {
    const sha = await this.getFileSha(token, owner, repo, path, branch);
    const body: any = {
      message: commitMessage,
      content: this.toBase64(content),
      branch,
    };
    if (sha) body.sha = sha;

    if (commitDate) {
      const username = githubUsername || 'leetie-user';
      const cleanEmail = `${username.toLowerCase()}@users.noreply.github.com`;
      const committerObj = {
        name: username,
        email: cleanEmail,
        date: commitDate,
      };
      body.author = committerObj;
      body.committer = committerObj;
    }

    const res = await fetch(`${this.BASE_URL}/repos/${owner}/${repo}/contents/${path}`, {
      method: 'PUT',
      headers: this.getHeaders(token),
      body: JSON.stringify(body),
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      const errorMsg = errorData.message || errorData.error || `HTTP ${res.status}`;
      console.error(`[leetie] GitHub API PUT error (${res.status}) on ${path}:`, errorData);
      throw new Error(`GitHub API Error (${res.status}): ${errorMsg}`);
    }

    const resData = await res.json();
    return resData.content?.sha || resData.commit?.sha || 'success';
  }

  static generateReadmeContent(commits: CommitRecord[], config: ExtensionConfig): string {
    const subfolder = config.solutionSubfolder || 'solutions';
    const rows = commits
      .map((c) => {
        const link = `https://leetcode.com/problems/${c.problemSlug}/`;
        const solutionLink = `./${c.githubPath.replace(`${subfolder}/`, '')}`;
        return `| ${c.problemSlug} | ${c.problemTitle} | ${c.difficulty} | ${c.lang} | [Problem](${link}) | [Solution](${solutionLink}) |`;
      })
      .join('\n');

    return `# My LeetCode Solutions

> *Automatically synced by [leetie](https://github.com/Shreeprasandh/leetie).*

## Progress Summary: ${commits.length} Solved

| ID | Problem | Difficulty | Language | Problem Link | Solution Code |
|---|---------|-----------|----------|--------------|---------------|
${rows}
`;
  }

  static async commitSubmission(submission: Submission): Promise<CommitRecord> {
    const config = await storage.getConfig();

    if (!config.githubToken || !config.githubUsername) {
      throw new Error('GitHub token or username not configured. Please open leetie options.');
    }

    // Ensure repo exists
    await GitHubService.ensureRepo(config.githubToken, config.githubUsername, config.repoName);

    const filePath = this.formatFilePath(submission, config);
    const fileContent = config.addHeaderComment ? this.formatSolutionHeader(submission) : submission.code;
    const commitMessage = `leetie: Add ${submission.problem.id}. ${submission.problem.title} [${submission.problem.difficulty}] (${submission.lang})`;
    const commitDate = submission.timestamp ? new Date(submission.timestamp).toISOString() : undefined;

    // Commit solution file
    const sha = await this.putFile(
      config.githubToken,
      config.githubUsername,
      config.repoName,
      filePath,
      fileContent,
      commitMessage,
      config.branch,
      commitDate,
      config.githubUsername
    );

    const record: CommitRecord = {
      submissionId: submission.submissionId,
      problemSlug: submission.problem.slug,
      problemTitle: submission.problem.title,
      difficulty: submission.problem.difficulty,
      commitSha: sha,
      githubPath: filePath,
      committedAt: Date.now(),
      lang: submission.lang,
    };

    // Save record to local storage
    const updatedCommits = await storage.addCommit(record);

    // Auto-update README if enabled
    if (config.autoReadme) {
      try {
        const readmePath = `${config.solutionSubfolder || 'solutions'}/README.md`;
        const readmeContent = this.generateReadmeContent(updatedCommits, config);
        await this.putFile(
          config.githubToken,
          config.githubUsername,
          config.repoName,
          readmePath,
          readmeContent,
          `leetie: Update solutions index README`,
          config.branch
        );
      } catch (err) {
        console.warn('[leetie] Auto README update failed:', err);
      }
    }

    return record;
  }
}
