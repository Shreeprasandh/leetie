# leetie — Master Project Plan
> **Version**: 1.0.0 — Authored by Luna  
> **Status**: Planning Phase  
> **Classification**: Internal Working Document

---

## Table of Contents

1. [Project Identity](#1-project-identity)
2. [Legal & Ethical Framework](#2-legal--ethical-framework)
3. [Core Concept & Vision](#3-core-concept--vision)
4. [Technical Architecture](#4-technical-architecture)
5. [Browser Extension Structure](#5-browser-extension-structure)
6. [GitHub OAuth Flow](#6-github-oauth-flow)
7. [LeetCode Submission Detection](#7-leetcode-submission-detection)
8. [Historical Submission Recovery](#8-historical-submission-recovery)
9. [GitHub File Commit Engine](#9-github-file-commit-engine)
10. [Repository & Directory Schema](#10-repository--directory-schema)
11. [UI/UX Design System](#11-uiux-design-system)
12. [Data Models & Contracts](#12-data-models--contracts)
13. [Storage & State Management](#13-storage--state-management)
14. [Security Model](#14-security-model)
15. [Error Handling & Resilience](#15-error-handling--resilience)
16. [Development Phases & Milestones](#16-development-phases--milestones)
17. [Tech Stack & Dependencies](#17-tech-stack--dependencies)
18. [Testing Strategy](#18-testing-strategy)
19. [Build & Release Pipeline](#19-build--release-pipeline)
20. [Future Roadmap](#20-future-roadmap)

---

## 1. Project Identity

| Field | Value |
|---|---|
| **Name** | `leetie` |
| **Tagline** | *Every solution. Automatically archived.* |
| **Type** | Browser Extension (Chrome, Edge, Firefox) |
| **Target Audience** | Competitive programmers, job seekers, CS students |
| **Core Value** | Zero-friction automatic GitHub archiving of accepted LeetCode solutions |
| **License** | MIT |
| **Inspired By** | Personal need — independent implementation |

### Philosophy

leetie is built on three principles:
- **Invisible by default**: It should never interrupt your solving flow. It activates only when you succeed.
- **Ownership first**: Your solutions, your repository, your data. leetie never touches a third-party server.
- **Honest engineering**: Every integration is performed through official, documented public APIs and browser extension standards. No scraping, no session abuse, no gray-area automation.

---

## 2. Legal & Ethical Framework

> [!IMPORTANT]
> This section is non-negotiable. Every implementation decision must be filtered through these constraints. When in doubt — stay on the legal side. Always.

### 2.1 LeetCode Terms of Service Compliance

LeetCode holds copyright over all problem statements, descriptions, examples, constraints, and editorial content. leetie has a strict zero-tolerance policy on this:

- **NEVER store, commit, or transmit problem descriptions, statements, examples, constraints, or editorial content** in any form — not in files, not in comments, not in metadata.
- **Only the user's own authored solution code** is ever committed. The user is the sole author and copyright holder of that code.
- The **problem link** (`https://leetcode.com/problems/{slug}/`) is included in the file header as a reference. A URL is not copyrightable content — it is a pointer back to LeetCode's own page, which benefits them.
- Using the **publicly documented LeetCode GraphQL API endpoints** that are accessible to authenticated users within their own session — the same way the website itself operates. No credential harvesting, no third-party proxies.
- Operating exclusively within the **user's authenticated browser session** — accessing only data the user themselves would see when logged in.
- Never making requests beyond what a logged-in user can do manually.

**Legal Basis**: A user committing their own authored code to their own private/public GitHub repository is lawful under standard copyright principles (you are the author of the code you write). Including a URL link back to LeetCode is standard and non-infringing practice.

### 2.2 GitHub API Usage Compliance

- leetie uses the official **GitHub REST API v3** exclusively.
- Authentication via **GitHub OAuth App** (public, documented flow).
- Respects **rate limits** (5,000 requests/hour authenticated) with built-in backoff.
- Requires only minimum necessary OAuth scopes (`repo` or `public_repo`).

### 2.3 Browser Extension Store Policies

- Chrome Web Store & Edge Add-ons: No hidden data collection, no remote code execution.
- All permissions declared in `manifest.json` with clear justification.
- No use of `eval()` or dynamically injected remote scripts.

### 2.4 No Copyright Infringement

- leetie is an **original implementation**. No code is copied from any existing extension.
- Architectural concepts are built from first principles using public API documentation.
- All third-party libraries must be open-source with MIT / Apache-2.0 compatible licenses.

---

## 3. Core Concept & Vision

### What leetie does

```
User solves a problem on LeetCode
         ↓
leetie detects the "Accepted" submission event
         ↓
Extracts: problem metadata + user's solution code
         ↓
Formats into a clean directory structure
         ↓
Commits to the user's GitHub repository via REST API
         ↓
User gets a beautiful, organized archive — automatically
```

### What leetie does NOT do

- Does NOT store any data on external servers
- Does NOT transmit your LeetCode session to any third party
- Does NOT commit problem descriptions (ToS compliance)
- Does NOT run background polling while you're not on LeetCode
- Does NOT require any backend service — 100% client-side

---

## 4. Technical Architecture

### 4.1 High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────┐
│                   BROWSER                        │
│                                                  │
│  ┌──────────────┐      ┌──────────────────────┐  │
│  │  Content     │      │   Background         │  │
│  │  Script      │◄────►│   Service Worker     │  │
│  │  (leetcode)  │      │   (event hub)        │  │
│  └──────────────┘      └──────────┬───────────┘  │
│                                   │              │
│  ┌──────────────┐                 │              │
│  │  Popup UI    │◄────────────────┘              │
│  │  (React)     │      ┌──────────────────────┐  │
│  └──────────────┘      │   chrome.storage     │  │
│                        │   (local encrypted)  │  │
│                        └──────────────────────┘  │
└────────────────────────────┬────────────────────┘
                             │ HTTPS only
              ┌──────────────┴──────────────┐
              │                             │
    ┌─────────▼──────────┐     ┌────────────▼────────────┐
    │   GitHub REST API  │     │  LeetCode GraphQL API   │
    │   api.github.com   │     │  leetcode.com/graphql   │
    └────────────────────┘     └─────────────────────────┘
```

### 4.2 Extension Components

| Component | Responsibility |
|---|---|
| **Manifest v3** | Permission declarations, extension metadata |
| **Background Service Worker** | Event coordination, API calls, token storage |
| **Content Script** | DOM observation, network request interception on leetcode.com |
| **Popup UI** | User-facing dashboard, auth controls, sync status |
| **Options Page** | Repository selection, commit format config, language prefs |

---

## 5. Browser Extension Structure

### 5.1 Directory Layout

```
leetie/
├── manifest.json                    # MV3 manifest
├── package.json
├── tsconfig.json
├── vite.config.ts                   # Build system
│
├── src/
│   ├── background/
│   │   ├── index.ts                 # Service worker entry
│   │   ├── github.service.ts        # GitHub API layer
│   │   ├── leetcode.service.ts      # LeetCode API layer
│   │   ├── sync.service.ts          # Commit orchestration
│   │   ├── recovery.service.ts      # Historical recovery engine
│   │   └── auth.service.ts          # OAuth token management
│   │
│   ├── content/
│   │   ├── index.ts                 # Content script entry
│   │   ├── interceptor.ts           # fetch/XHR interception
│   │   ├── observer.ts              # DOM mutation observer
│   │   └── extractor.ts             # Data extraction logic
│   │
│   ├── popup/
│   │   ├── index.html
│   │   ├── main.tsx                 # React entry
│   │   ├── App.tsx
│   │   └── components/
│   │       ├── StatusBadge.tsx
│   │       ├── RecentCommits.tsx
│   │       ├── ConnectGitHub.tsx
│   │       └── SyncProgress.tsx
│   │
│   ├── options/
│   │   ├── index.html
│   │   ├── main.tsx
│   │   └── components/
│   │       ├── RepoSelector.tsx
│   │       ├── CommitFormat.tsx
│   │       └── LanguageMap.tsx
│   │
│   ├── shared/
│   │   ├── types.ts                 # Global TypeScript types
│   │   ├── constants.ts             # API endpoints, config
│   │   ├── messages.ts              # Chrome message contracts
│   │   ├── utils.ts                 # Shared utilities
│   │   └── storage.ts               # chrome.storage wrapper
│   │
│   └── assets/
│       ├── icon-16.png
│       ├── icon-32.png
│       ├── icon-48.png
│       └── icon-128.png
│
├── public/
└── dist/                            # Build output (gitignored)
```

### 5.2 manifest.json (MV3)

```json
{
  "manifest_version": 3,
  "name": "leetie",
  "version": "1.0.0",
  "description": "Automatically sync your accepted LeetCode solutions to GitHub.",
  "permissions": [
    "storage",
    "identity",
    "tabs",
    "notifications"
  ],
  "host_permissions": [
    "https://leetcode.com/*",
    "https://api.github.com/*"
  ],
  "background": {
    "service_worker": "background/index.js",
    "type": "module"
  },
  "content_scripts": [
    {
      "matches": ["https://leetcode.com/problems/*"],
      "js": ["content/index.js"],
      "run_at": "document_idle"
    }
  ],
  "action": {
    "default_popup": "popup/index.html",
    "default_icon": {
      "16": "assets/icon-16.png",
      "32": "assets/icon-32.png",
      "48": "assets/icon-48.png",
      "128": "assets/icon-128.png"
    }
  },
  "options_page": "options/index.html",
  "icons": {
    "48": "assets/icon-48.png",
    "128": "assets/icon-128.png"
  }
}
```

---

## 6. GitHub OAuth Flow

> [!NOTE]
> We use a **GitHub OAuth App** (not a GitHub App). This is the correct choice for a browser extension that acts on behalf of the user. All tokens are stored locally in `chrome.storage.local` — never on a server.

### 6.1 OAuth Flow Diagram

```
User clicks "Connect GitHub"
        ↓
Extension opens OAuth popup tab
  → https://github.com/login/oauth/authorize
    ?client_id=<LEETIE_CLIENT_ID>
    &scope=repo
    &redirect_uri=https://leetie.github.io/callback
    &state=<RANDOM_CSRF_TOKEN>
        ↓
User authorizes on GitHub
        ↓
GitHub redirects to callback page with ?code=<AUTH_CODE>&state=<STATE>
        ↓
Callback page posts message to extension (window.postMessage)
        ↓
Background worker receives code, validates state
        ↓
Background worker exchanges code for access_token
  → POST https://github.com/login/oauth/access_token
    (via a lightweight CORS proxy we self-host OR via GitHub's PKCE-compatible approach)
        ↓
Token stored in chrome.storage.local (encrypted)
        ↓
Extension calls GET /user to confirm identity
        ↓
Connected state saved — GitHub username displayed in popup
```

> [!IMPORTANT]
> **CORS Challenge**: GitHub's `/login/oauth/access_token` endpoint does not support CORS from browser extensions. **Solution**: We deploy a minimal, open-source, self-hostable token exchange proxy (a single Vercel serverless function — ~15 lines) that the extension calls. The proxy does nothing except forward the code to GitHub and return the token. The proxy source is included in the repo, is fully auditable, and any user can self-host their own instance. The extension's default proxy endpoint is configurable in options.

### 6.2 Required OAuth Scopes

| Scope | Reason |
|---|---|
| `repo` | Full access to private & public repos for committing files |
| `public_repo` | Alternative if user only wants to use a public repository |

We request `public_repo` by default and promote to `repo` only if the user selects a private repository.

### 6.3 Token Exchange Proxy (Serverless)

```typescript
// vercel/api/exchange.ts — fully open source, user-auditable
export default async function handler(req, res) {
  const { code } = req.body;
  const response = await fetch('https://github.com/login/oauth/access_token', {
    method: 'POST',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({
      client_id: process.env.GITHUB_CLIENT_ID,
      client_secret: process.env.GITHUB_CLIENT_SECRET,
      code,
    }),
  });
  const data = await response.json();
  res.json({ access_token: data.access_token });
}
```

---

## 7. LeetCode Submission Detection

> [!NOTE]
> We intercept the browser's native `fetch` API within the content script — the same mechanism a browser's DevTools Network panel uses. This is a standard, well-documented browser API technique. We do NOT inject scripts into LeetCode's page bundle or modify their application code.

### 7.1 The Interception Strategy

LeetCode uses GraphQL for all client-server communication. When a user submits a solution, the browser sends a POST to `https://leetcode.com/graphql/` containing a submission mutation, and then subsequently polls for the result.

We intercept the **result polling response** — specifically the `checkSubmission` or equivalent query that returns `statusDisplay: "Accepted"`.

```typescript
// content/interceptor.ts
const originalFetch = window.fetch.bind(window);

window.fetch = async function (input, init) {
  const response = await originalFetch(input, init);

  if (isLeetCodeGraphQL(input)) {
    // Clone response (streams can only be consumed once)
    const clone = response.clone();
    clone.json().then((data) => {
      if (isAcceptedSubmission(data)) {
        handleAcceptedSubmission(data);
      }
    }).catch(() => {}); // Silently ignore non-JSON or irrelevant responses
  }

  return response; // Always return original, unmodified response
};
```

### 7.2 Accepted Submission Detection Logic

```typescript
function isAcceptedSubmission(data: unknown): boolean {
  // Works for both checkSubmission and submissionDetails response shapes
  const status =
    data?.data?.submissionDetails?.statusDisplay ||
    data?.data?.submissionResult?.statusDisplay;
  return status === 'Accepted';
}
```

### 7.3 Data Extraction from Submission Response

When an accepted submission is detected, we extract:

```typescript
interface RawSubmission {
  id: string;                  // Submission ID (for deduplication)
  lang: string;                // "python3", "javascript", "java", etc.
  code: string;                // User's authored solution source code
  runtime: string;             // e.g. "72 ms"
  memory: string;              // e.g. "16.4 MB"
  runtimePercentile: number;   // Beats X%
  memoryPercentile: number;    // Beats Y%
  timestamp: number;           // Unix timestamp
}
```

Additionally, we read problem metadata from the page URL and DOM:

```typescript
interface ProblemMeta {
  slug: string;        // "two-sum" (from URL)
  title: string;       // "Two Sum" (from page title or DOM)
  id: string;          // "1" (from problem header)
  difficulty: string;  // "Easy" / "Medium" / "Hard"
  topicTags: string[]; // ["Array", "Hash Table"] — from GraphQL response
}
```

### 7.4 Fallback: Monaco Editor Code Extraction

In edge cases where code is not present in the submission response, we extract it directly from the Monaco Editor instance:

```typescript
function extractFromMonaco(): string | null {
  const editorInstance = window.monaco?.editor?.getModels?.()?.[0];
  return editorInstance?.getValue() ?? null;
}
```

---

## 8. Historical Submission Recovery

> [!NOTE]
> This feature allows Sir to recover all 250+ previously accepted submissions. It uses LeetCode's GraphQL API with the user's own authenticated session cookies — no credentials are harvested or stored. This is equivalent to what the LeetCode website itself does.

### 8.1 Recovery Flow

```
User clicks "Recover History" in popup
        ↓
Background worker reads session cookies from leetcode.com
  (using chrome.cookies API — requires "cookies" permission)
        ↓
Paginated GraphQL query loop to fetch all accepted submissions
        ↓
For each submission ID, fetch full submission detail (with code)
        ↓
Deduplicate: one commit per problem (latest accepted)
        ↓
Batch-commit to GitHub with rate-limit awareness
        ↓
Progress bar updates in real-time via chrome.runtime.sendMessage
        ↓
Final report: X problems recovered, Y already existed
```

### 8.2 LeetCode GraphQL Queries Used

**Query 1: Fetch Submission History (Paginated)**

```graphql
query leetieSubmissionHistory($offset: Int!, $limit: Int!) {
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
```

**Query 2: Fetch Full Submission Detail**

```graphql
query leetieSubmissionDetail($id: Int!) {
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
```

### 8.3 Recovery Algorithm

```typescript
async function recoverHistory(): Promise<void> {
  const allAccepted: SubmissionRecord[] = [];
  let offset = 0;
  const LIMIT = 20; // LeetCode's max per page

  // Phase 1: Collect all submission IDs
  while (true) {
    const page = await fetchSubmissionList(offset, LIMIT);
    const accepted = page.submissions.filter(s => s.statusDisplay === 'Accepted');
    allAccepted.push(...accepted);
    if (!page.hasNext) break;
    offset += LIMIT;
    await sleep(300); // Polite delay — respect server load
  }

  // Phase 2: Deduplicate — keep only the latest per problem slug
  const latestBySlug = deduplicateBySlug(allAccepted);

  // Phase 3: Fetch full code for each unique problem
  const total = latestBySlug.length;
  for (let i = 0; i < total; i++) {
    const detail = await fetchSubmissionDetail(latestBySlug[i].id);
    await commitToGitHub(detail);
    sendProgressUpdate({ current: i + 1, total });
    await sleep(500); // Rate limit awareness
  }
}
```

### 8.4 Deduplication Strategy

For problems solved in multiple languages, we keep **one file per language per problem**. If a problem was solved multiple times in the same language, we keep the **most recent accepted submission**.

---

## 9. GitHub File Commit Engine

### 9.1 Commit Flow

```typescript
async function commitSolution(submission: ProcessedSubmission): Promise<void> {
  const { path, content, commitMessage } = formatCommit(submission);
  
  // Check if file already exists (to get SHA for updates)
  const existing = await getFileInfo(path);
  
  await putFileContent({
    path,
    content: btoa(unescape(encodeURIComponent(content))), // UTF-8 safe base64
    message: commitMessage,
    sha: existing?.sha, // Required for updates, omit for new files
    branch: config.branch, // default: 'main'
  });
}
```

### 9.2 GitHub REST API Calls

| Operation | Method | Endpoint |
|---|---|---|
| Verify token | `GET` | `/user` |
| List repositories | `GET` | `/user/repos?per_page=100` |
| Create repository | `POST` | `/user/repos` |
| Get file (for SHA) | `GET` | `/repos/{owner}/{repo}/contents/{path}` |
| Create/Update file | `PUT` | `/repos/{owner}/{repo}/contents/{path}` |
| Get README | `GET` | `/repos/{owner}/{repo}/contents/README.md` |

### 9.3 Commit Message Format

Configurable by the user. Default template:

```
leetie: Add {problemId}. {problemTitle} [{difficulty}] ({language})
```

Examples:
```
leetie: Add 1. Two Sum [Easy] (Python3)
leetie: Add 146. LRU Cache [Medium] (Java)
leetie: Add 42. Trapping Rain Water [Hard] (C++)
```

### 9.4 Auto README Generation

leetie can auto-generate and maintain a `README.md` in the root of the solutions repo:

```markdown
# My LeetCode Solutions

Automatically synced by [leetie](https://github.com/you/leetie).

## Progress: 251 / 3000+ solved

| # | Problem | Difficulty | Language | Solution |
|---|---------|-----------|----------|---------|
| 1 | Two Sum | Easy | Python3 | [View](./Easy/0001-Two_Sum/solution.py) |
...
```

This README is regenerated on every new commit, keeping it perpetually up to date.

---

## 10. Repository & Directory Schema

### 10.1 Default Directory Structure (in the user's GitHub repo)

```
leetie/                          ← Repository root (Shreeprasandh/leetie)
├── src/                         ← Extension source code
├── manifest.json
├── package.json
│
└── solutions/                   ← Auto-committed solutions folder
    ├── README.md                ← Auto-generated progress table
    │
    ├── Easy/
    │   ├── 0001-Two_Sum/
    │   │   └── solution.py
    │   ├── 0020-Valid_Parentheses/
    │   │   ├── solution.py
    │   │   └── solution.js      ← Multiple languages supported
    │   └── ...
    │
    ├── Medium/
    │   ├── 0002-Add_Two_Numbers/
    │   │   └── solution.java
    │   └── ...
    │
    └── Hard/
        ├── 0042-Trapping_Rain_Water/
        │   └── solution.cpp
        └── ...
```

### 10.2 File Naming Convention

```
{difficulty}/{problemId_padded}-{Title_Snake_Case}/solution.{ext}
```

| Language | Extension |
|---|---|
| Python3 / Python | `.py` |
| JavaScript | `.js` |
| TypeScript | `.ts` |
| Java | `.java` |
| C++ | `.cpp` |
| C | `.c` |
| C# | `.cs` |
| Go | `.go` |
| Rust | `.rs` |
| Kotlin | `.kt` |
| Swift | `.swift` |
| Ruby | `.rb` |
| Scala | `.scala` |
| PHP | `.php` |

### 10.3 Solution File Header (Optional Metadata Comment)

Each committed file begins with a comment block (toggleable in options).

> [!IMPORTANT]
> The header must NEVER include problem descriptions, examples, constraints, or any LeetCode-owned content. Only the link (a URL, not content) is permitted as a reference.

```python
# ──────────────────────────────────────────────────
# Problem  : 1. Two Sum
# Difficulty: Easy
# Tags     : Array, Hash Table
# Link     : https://leetcode.com/problems/two-sum/
# Runtime  : 72 ms (beats 91.3%)
# Memory   : 16.4 MB (beats 72.1%)
# Language : Python3
# Synced by: leetie
# ──────────────────────────────────────────────────

class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        ...
```

---

## 11. UI/UX Design System

### 11.1 Visual Identity

| Token | Value |
|---|---|
| **Primary Color** | `hsl(222, 89%, 55%)` — a confident, deep blue |
| **Accent** | `hsl(142, 71%, 45%)` — clean green (success / accepted) |
| **Warning** | `hsl(38, 92%, 50%)` — amber |
| **Error** | `hsl(0, 72%, 51%)` — red |
| **Background** | `hsl(224, 20%, 10%)` — near-black |
| **Surface** | `hsl(224, 16%, 14%)` — card surface |
| **Border** | `hsl(224, 12%, 22%)` — subtle |
| **Text Primary** | `hsl(220, 20%, 95%)` |
| **Text Muted** | `hsl(220, 10%, 55%)` |
| **Font** | `Inter` (UI text) + `JetBrains Mono` (code/paths) |
| **Border Radius** | `8px` (cards), `6px` (buttons), `4px` (inputs) |

### 11.2 Popup Dimensions

- **Width**: `360px` (fixed)
- **Min-height**: `440px`
- **Max-height**: `600px`

### 11.3 Popup Screens

**Screen 1 — Unauthenticated**
```
┌─────────────────────────────────────┐
│  leetie                    ●        │
│  ─────────────────────────────────  │
│                                     │
│     Connect GitHub to start         │
│     auto-archiving your accepted    │
│     LeetCode solutions.             │
│                                     │
│  ┌─────────────────────────────┐    │
│  │   Connect GitHub            │    │
│  └─────────────────────────────┘    │
│                                     │
│  Your data stays in your browser.   │
└─────────────────────────────────────┘
```

**Screen 2 — Authenticated, Active**
```
┌─────────────────────────────────────┐
│  leetie            @username  ● Live │
│  ─────────────────────────────────  │
│  ✔ leetcode-solutions          main │
│                                     │
│  ┌ Recent Commits ──────────────┐   │
│  │ ✔ 1. Two Sum       2 min ago │   │
│  │ ✔ 146. LRU Cache   1 hr ago  │   │
│  │ ✔ 42. Rain Water  Yesterday  │   │
│  └──────────────────────────────┘   │
│                                     │
│  251 solved · 251 synced            │
│                                     │
│  [Recover History]  [⚙ Settings]   │
└─────────────────────────────────────┘
```

**Screen 3 — Recovery In Progress**
```
┌─────────────────────────────────────┐
│  leetie            @username  ● Sync │
│  ─────────────────────────────────  │
│  Recovering history...              │
│                                     │
│  ████████████░░░░░░░░  127 / 251    │
│                                     │
│  Currently: 42. Trapping Rain Water │
│  ETA: ~3 min remaining              │
│                                     │
│  [Stop Recovery]                    │
└─────────────────────────────────────┘
```

### 11.4 Micro-interactions

- Status dot pulses subtly when a new commit is being pushed
- Commit items in the list slide in from the right with a fade
- Progress bar fills with a smooth `ease-out` animation
- Button press has a 2px `translateY` press effect
- Toast notification slides in from the bottom-right on successful commit

---

## 12. Data Models & Contracts

### 12.1 Core TypeScript Types (`shared/types.ts`)

```typescript
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
  commitSha: string;
  githubPath: string;
  committedAt: number;
  lang: string;
}

export interface ExtensionConfig {
  githubToken: string;       // Encrypted
  githubUsername: string;
  repoName: string;
  branch: string;            // default: 'main'
  addHeaderComment: boolean; // default: true
  autoReadme: boolean;       // default: true
  preferredDirFormat: 'difficulty-first' | 'flat'; // default: 'difficulty-first'
  proxyUrl: string;          // Token exchange proxy URL
}

export interface ExtensionState {
  isAuthenticated: boolean;
  syncStatus: SyncStatus;
  totalSynced: number;
  recentCommits: CommitRecord[];
  lastError: string | null;
  recoveryProgress: { current: number; total: number } | null;
}
```

### 12.2 Chrome Message Protocol (`shared/messages.ts`)

```typescript
export type MessageType =
  | 'SUBMISSION_DETECTED'
  | 'COMMIT_SUCCESS'
  | 'COMMIT_ERROR'
  | 'RECOVERY_START'
  | 'RECOVERY_PROGRESS'
  | 'RECOVERY_COMPLETE'
  | 'AUTH_SUCCESS'
  | 'AUTH_ERROR'
  | 'GET_STATE';

export interface Message<T = unknown> {
  type: MessageType;
  payload: T;
}
```

---

## 13. Storage & State Management

### 13.1 `chrome.storage.local` Schema

```typescript
interface StorageSchema {
  // Auth (sensitive — see Security section)
  'leetie.token': string;          // Encrypted GitHub access token
  'leetie.username': string;       // GitHub username
  
  // Config
  'leetie.config': ExtensionConfig;
  
  // State
  'leetie.state': ExtensionState;
  
  // Sync history (capped at last 100 entries)
  'leetie.commits': CommitRecord[];
  
  // Deduplication index
  'leetie.syncedSlugs': Record<string, string>; // slug → commitSha
}
```

### 13.2 Token Encryption

The GitHub access token is encrypted at rest using the **Web Crypto API** (`AES-GCM`), with the encryption key derived from the user's Chrome profile identity. This means even if `chrome.storage.local` data were extracted, the token would be unusable without the same browser profile.

```typescript
async function encryptToken(token: string): Promise<string> {
  const key = await deriveKeyFromBrowserIdentity();
  const encrypted = await crypto.subtle.encrypt(
    { name: 'AES-GCM', iv: crypto.getRandomValues(new Uint8Array(12)) },
    key,
    new TextEncoder().encode(token)
  );
  return bufferToBase64(encrypted);
}
```

---

## 14. Security Model

| Threat | Mitigation |
|---|---|
| Token leakage via XSS | Token stored encrypted in `chrome.storage.local`, never in DOM or `window` |
| Man-in-the-middle | All API calls over HTTPS only; `fetch` with no custom credentials mode |
| Malicious page reading extension data | `chrome.storage` is sandboxed — webpage scripts cannot access it |
| Proxy server stealing auth code | Proxy source is open and auditable; code is single-use and expires in 10 min |
| Accidental token exposure in logs | All logging stripped in production build via Vite define |
| Rate limit abuse | Built-in backoff: 300ms between recovery requests, exponential backoff on 429 |
| Session cookie misuse | Session cookies are read-only within the extension context and never transmitted to any server other than `leetcode.com` directly |

---

## 15. Error Handling & Resilience

### 15.1 Error Categories

| Category | Recovery Strategy |
|---|---|
| Network timeout | Retry up to 3× with exponential backoff (1s, 2s, 4s) |
| GitHub 401 Unauthorized | Clear token, prompt re-authentication |
| GitHub 409 Conflict | Re-fetch file SHA and retry commit |
| GitHub 422 (already exists) | Fetch SHA, update instead of create |
| LeetCode GraphQL errors | Log and skip (do not crash detection loop) |
| Rate limit (GitHub 403/429) | Parse `X-RateLimit-Reset` header, queue and resume |
| Recovery interrupted | Save progress checkpoint to storage, allow resume |

### 15.2 Resilient Recovery

If a recovery session is interrupted (browser closed, etc.), the progress checkpoint is persisted in `chrome.storage.local`. On next popup open, the user is offered to **resume** from where they left off.

---

## 16. Development Phases & Milestones

### Phase 0: Foundation ✦ Week 1
- [ ] Initialize project with Vite + React + TypeScript
- [ ] Configure Manifest V3 with all required permissions
- [ ] Implement `chrome.storage` abstraction layer
- [ ] Set up build pipeline (Vite → `dist/` → zip for store)
- [ ] Create base design system (CSS vars, typography, component shells)

### Phase 1: GitHub Auth ✦ Week 1-2
- [ ] Deploy token exchange proxy (Vercel serverless)
- [ ] Implement OAuth popup flow
- [ ] Token encryption/decryption module
- [ ] Repository listing and selection UI
- [ ] Auto-create repository if not exists

### Phase 2: Submission Detection ✦ Week 2-3
- [ ] `fetch` interception in content script
- [ ] Accepted submission detection logic
- [ ] Problem metadata extraction
- [ ] Monaco editor fallback extraction
- [ ] Message passing: content script → background worker

### Phase 3: GitHub Commit Engine ✦ Week 3
- [ ] File path formatter
- [ ] Base64 encoding utility (UTF-8 safe)
- [ ] `PUT /repos/.../contents/...` implementation
- [ ] SHA conflict resolution
- [ ] Commit message template engine
- [ ] Solution file header comment generator

### Phase 4: Popup UI ✦ Week 3-4
- [ ] Unauthenticated screen
- [ ] Authenticated dashboard screen
- [ ] Recent commits list with animations
- [ ] Status indicator (Live / Syncing / Error)
- [ ] Settings/Options page

### Phase 5: History Recovery ✦ Week 4-5
- [ ] Submission list paginator
- [ ] Submission detail fetcher
- [ ] Deduplication engine
- [ ] Batch commit with rate limit awareness
- [ ] Progress UI with ETA estimation
- [ ] Checkpoint/resume logic

### Phase 6: Polish & README Generation ✦ Week 5-6
- [ ] Auto README generation and update
- [ ] Notification toasts on successful commits
- [ ] Error UI with actionable messages
- [ ] Options page (all config fields)
- [ ] Edge/Firefox compatibility pass

### Phase 7: Testing & Store Submission ✦ Week 6-7
- [ ] Unit tests for all services
- [ ] Integration tests (mocked GitHub/LeetCode APIs)
- [ ] Manual QA on Chrome, Edge
- [ ] Privacy policy page
- [ ] Chrome Web Store submission
- [ ] Edge Add-ons submission

---

## 17. Tech Stack & Dependencies

### 17.1 Core Stack

| Technology | Version | Purpose |
|---|---|---|
| TypeScript | `^5.5` | Type safety throughout |
| React | `^18.3` | Popup / Options UI |
| Vite | `^5.x` | Build tooling with CRXJS plugin |
| CRXJS Vite Plugin | `^2.x` | Chrome extension hot reload |

### 17.2 UI Libraries

| Library | Version | Purpose |
|---|---|---|
| `lucide-react` | latest | Icon set |
| `framer-motion` | `^11.x` | Micro-animations |
| `clsx` | `^2.x` | Conditional class names |

### 17.3 Development Tools

| Tool | Purpose |
|---|---|
| `vitest` | Unit testing |
| `@testing-library/react` | React component testing |
| `msw` (Mock Service Worker) | API mocking for tests |
| `eslint` + `@typescript-eslint` | Linting |
| `prettier` | Code formatting |
| `husky` + `lint-staged` | Pre-commit hooks |

### 17.4 Serverless Proxy

| Technology | Purpose |
|---|---|
| Vercel Functions | Token exchange proxy hosting |
| Node.js runtime | Serverless function runtime |

### 17.5 Zero-Dependency Policy

The background service worker and content script have **zero runtime dependencies**. All GitHub and LeetCode API calls use native `fetch`. This minimizes bundle size, attack surface, and Manifest V3 compatibility issues.

---

## 18. Testing Strategy

### 18.1 Unit Tests

- `github.service.ts` — mock all API calls, test commit logic, SHA resolution, rate limit handling
- `interceptor.ts` — mock `window.fetch`, test detection accuracy
- `extractor.ts` — test with real GraphQL response fixtures
- `recovery.service.ts` — test pagination, deduplication, checkpoint logic
- `auth.service.ts` — test token encryption/decryption round-trips

### 18.2 Integration Tests

- Full OAuth flow with mocked GitHub responses
- Submission detected → extracted → committed (end-to-end mock)
- Recovery: 250 submissions paginated → deduplicated → committed

### 18.3 Manual QA Checklist

- [ ] Extension loads with no console errors
- [ ] OAuth flow completes and token is stored
- [ ] Submitting an Easy problem auto-commits correctly
- [ ] Submitting in 3+ different languages works
- [ ] History recovery runs to completion without rate limit errors
- [ ] Popup UI renders correctly at all states
- [ ] Options page saves and persists settings
- [ ] Extension works after browser restart (token survives)
- [ ] Correct behavior on non-LeetCode pages (no errors)

---

## 19. Build & Release Pipeline

### 19.1 Build Commands

```bash
npm run dev        # Hot-reload dev build (CRXJS)
npm run build      # Production build → dist/
npm run test       # Vitest unit tests
npm run lint       # ESLint check
npm run zip        # Package dist/ → leetie-v{version}.zip for store upload
```

### 19.2 Environment Variables

```bash
# .env.local (never committed)
VITE_GITHUB_CLIENT_ID=<your_oauth_app_client_id>
VITE_TOKEN_PROXY_URL=https://leetie-proxy.vercel.app/api/exchange
```

### 19.3 Release Checklist

- [ ] `npm run lint` — zero errors
- [ ] `npm run test` — all passing
- [ ] `npm run build` — clean build
- [ ] Version bumped in `manifest.json` and `package.json`
- [ ] CHANGELOG updated
- [ ] Privacy policy URL live
- [ ] Store listing screenshots captured
- [ ] ZIP uploaded to Chrome Web Store Developer Dashboard
- [ ] ZIP uploaded to Edge Add-ons Developer Center

---

## 20. Future Roadmap

| Feature | Priority | Notes |
|---|---|---|
| **Firefox support** | High | WebExtension API compatibility layer |
| **Multi-repository routing** | Medium | Route different languages to different repos |
| **Stats dashboard** | Medium | Heatmap, tag breakdown, streak tracking |
| **Notion / Obsidian sync** | Low | Alternative export targets |
| **AI solution notes** | Low | Append Gemini-generated explanation as comments |
| **Problem tag filtering** | Low | Only sync problems with specific tags |
| **Custom directory formats** | Medium | User-defined folder schemas |
| **Team / org repository** | Low | Sync to a shared team repo |

---

> [!NOTE]
> This document is the single source of truth for leetie's development. Every implementation decision must trace back to a section in this plan. When in doubt — refer here first.

---

*Plan authored and maintained by Luna · For Sir's eyes only*
