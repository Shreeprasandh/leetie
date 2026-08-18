export interface GitHubUser {
  login: string;
  name: string;
  avatar_url: string;
  html_url: string;
}

export interface GitHubRepo {
  name: string;
  full_name: string;
  private: boolean;
  html_url: string;
}

export class GitHubService {
  private static BASE_URL = 'https://api.github.com';

  private static getHeaders(token: string) {
    return {
      Authorization: `Bearer ${token}`,
      Accept: 'application/vnd.github.v3+json',
      'User-Agent': 'leetie-extension',
    };
  }

  static async verifyUser(token: string): Promise<GitHubUser> {
    const res = await fetch(`${this.BASE_URL}/user`, {
      headers: this.getHeaders(token),
    });

    if (!res.ok) {
      if (res.status === 401) throw new Error('Invalid or expired GitHub token.');
      throw new Error(`GitHub API error: ${res.statusText}`);
    }

    const data = await res.json();
    return {
      login: data.login,
      name: data.name || data.login,
      avatar_url: data.avatar_url,
      html_url: data.html_url,
    };
  }

  static async getUserRepos(token: string): Promise<GitHubRepo[]> {
    const res = await fetch(`${this.BASE_URL}/user/repos?per_page=100&sort=updated`, {
      headers: this.getHeaders(token),
    });

    if (!res.ok) {
      throw new Error(`Failed to fetch repositories: ${res.statusText}`);
    }

    const data = await res.json();
    return data.map((repo: any) => ({
      name: repo.name,
      full_name: repo.full_name,
      private: repo.private,
      html_url: repo.html_url,
    }));
  }

  private static toBase64(str: string): string {
    const bytes = new TextEncoder().encode(str || '');
    let binary = '';
    for (const byte of bytes) binary += String.fromCharCode(byte);
    return btoa(binary);
  }

  static async ensureRepo(token: string, username: string, repoName: string): Promise<boolean> {
    const checkUrl = `${this.BASE_URL}/repos/${username}/${repoName}`;
    let checkRes = await fetch(checkUrl, { headers: this.getHeaders(token) });

    if (checkRes.status === 404) {
      // Determine if personal or org to use correct creation endpoint
      const userRes = await fetch(`${this.BASE_URL}/user`, { headers: this.getHeaders(token) });
      if (!userRes.ok) {
        throw new Error(`Failed to verify GitHub user profile (${userRes.status}): ${userRes.statusText}`);
      }
      const user = await userRes.json();
      const isPersonal = user.login?.toLowerCase() === username.toLowerCase();
      const createUrl = isPersonal ? `${this.BASE_URL}/user/repos` : `${this.BASE_URL}/orgs/${username}/repos`;

      // Create repo with auto_init: true
      const createRes = await fetch(createUrl, {
        method: 'POST',
        headers: {
          ...this.getHeaders(token),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name: repoName,
          description: 'My LeetCode solutions synced automatically by leetie.',
          private: false,
          auto_init: true,
          default_branch: 'main',
        }),
      });

      if (!createRes.ok) {
        throw new Error(`Failed to create repository '${repoName}': ${createRes.statusText}`);
      }

      // Poll until repo is initialized by GitHub (max 5 retries with 1s backoff)
      let initialized = false;
      for (let attempt = 0; attempt < 5; attempt++) {
        await new Promise((r) => setTimeout(r, 1000));
        checkRes = await fetch(checkUrl, { headers: this.getHeaders(token) });
        if (checkRes.ok) {
          initialized = true;
          break;
        }
      }
      if (!initialized) {
        console.warn('[leetie] Repository created but initialization polling timed out.');
      }
    }

    if (!checkRes.ok) {
      throw new Error(`Error verifying repository '${repoName}': ${checkRes.statusText}`);
    }

    const repoData = await checkRes.json();
    const defaultBranch = repoData.default_branch || 'main';

    // Verify if default branch tree exists, seed README if repository is still empty
    const treeRes = await fetch(`${this.BASE_URL}/repos/${username}/${repoName}/git/trees/${defaultBranch}`, {
      headers: this.getHeaders(token),
    });

    if (treeRes.status === 404 || treeRes.status === 409) {
      console.log('[leetie] Empty repository detected, seeding initial README.md...');
      const seedRes = await fetch(`${this.BASE_URL}/repos/${username}/${repoName}/contents/README.md`, {
        method: 'PUT',
        headers: {
          ...this.getHeaders(token),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          message: 'leetie: Initialize repository',
          content: this.toBase64('# My LeetCode Solutions\n\n> Automatically synced by [leetie](https://github.com/leetie/leetie).\n'),
          branch: defaultBranch,
        }),
      });
      if (!seedRes.ok) {
        console.warn('[leetie] Failed to seed initial README.md:', seedRes.statusText);
      } else {
        await new Promise((r) => setTimeout(r, 1000));
      }
    }

    return true;
  }

  static async fetchRepoTree(token: string, username: string, repoName: string, branch = 'main'): Promise<{ path: string; size?: number; sha?: string }[]> {
    const res = await fetch(`${this.BASE_URL}/repos/${username}/${repoName}/git/trees/${branch}?recursive=1`, {
      headers: this.getHeaders(token),
    });

    if (!res.ok) {
      if (res.status === 404) return [];
      throw new Error(`Failed to fetch repository tree (${res.status}): ${res.statusText}`);
    }

    const data = await res.json();
    if (!data?.tree || !Array.isArray(data.tree)) return [];
    return data.tree
      .filter((item: any) => item.type === 'blob')
      .map((item: any) => ({ path: item.path, size: item.size, sha: item.sha }));
  }
}
