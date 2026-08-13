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

  static async ensureRepo(token: string, username: string, repoName: string): Promise<boolean> {
    // Check if repo exists
    const checkRes = await fetch(`${this.BASE_URL}/repos/${username}/${repoName}`, {
      headers: this.getHeaders(token),
    });

    if (checkRes.ok) {
      return true; // Repo exists
    }

    if (checkRes.status === 404) {
      // Determine if personal or org to use correct creation endpoint
      const userRes = await fetch(`${this.BASE_URL}/user`, { headers: this.getHeaders(token) });
      if (!userRes.ok) {
        throw new Error(`Failed to verify GitHub user profile (${userRes.status}): ${userRes.statusText}`);
      }
      const user = await userRes.json();
      const isPersonal = user.login?.toLowerCase() === username.toLowerCase();
      const createUrl = isPersonal ? `${this.BASE_URL}/user/repos` : `${this.BASE_URL}/orgs/${username}/repos`;

      // Create repo
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
      // Brief delay to allow GitHub's async auto_init to populate default main branch
      await new Promise((r) => setTimeout(r, 1500));
      return true;
    }

    throw new Error(`Error verifying repository '${repoName}': ${checkRes.statusText}`);
  }
}
