# leetie — Project Brief

> *Every solution. Automatically archived.*

---

## What is it?

**leetie** is a browser extension for Chrome and Edge that silently watches your LeetCode sessions and automatically commits every accepted solution to a GitHub repository — organized, labelled, and timestamped — the moment you hit "Accepted."

No manual copying. No forgetting. No mess.

---

## The Problem it Solves

Every serious programmer accumulates hundreds of LeetCode solutions scattered across browser tabs, forgotten local files, or nowhere at all. When interview season hits, that work is invisible — to recruiters, to yourself, to anyone.

leetie turns your solving history into a living, public portfolio on GitHub. Automatically.

---

## Core Features

| Feature | Description |
|---|---|
| **Auto-sync** | Detects accepted submissions in real-time and commits them instantly |
| **History Recovery** | Bulk-imports all previously solved problems (250+ in your case) |
| **Clean directory structure** | Organized by difficulty → problem → language |
| **Auto README** | Generates and updates a progress table in your repo on every commit |
| **Solution headers** | Appends runtime, memory, and beats % as a comment in every file |
| **GitHub OAuth** | Secure, token-based auth — no passwords, no third-party servers |
| **100% local** | All data stays in your browser. leetie has no backend |

---

## How It Works

```
You get "Accepted" on LeetCode
        ↓
leetie intercepts the response (standard browser fetch API)
        ↓
Extracts your code + problem metadata
        ↓
Formats it into a clean file path
        ↓
Commits to your GitHub repo via REST API
        ↓
Done — silently, in under 2 seconds
```

---

## What It Does NOT Do

- No storing data on external servers
- No copying or redistributing LeetCode problem content (ToS safe)
- No backend or subscription required
- No interference with your solving experience

---

## Tech Snapshot

| Layer | Choice |
|---|---|
| **Type** | Browser Extension (Manifest V3) |
| **Language** | TypeScript |
| **UI** | React 18 + Framer Motion |
| **Build** | Vite + CRXJS |
| **Auth** | GitHub OAuth App |
| **APIs** | GitHub REST v3 · LeetCode GraphQL |
| **Storage** | `chrome.storage.local` (AES-GCM encrypted token) |
| **Proxy** | Vercel serverless (~15 lines, open source) |

---

## Timeline

| Phase | Scope | Target |
|---|---|---|
| 0 — Foundation | Project scaffold, build system, design tokens | Week 1 |
| 1 — Auth | GitHub OAuth, token storage, repo selection | Week 1–2 |
| 2 — Detection | Fetch interception, submission parsing | Week 2–3 |
| 3 — Commit Engine | File formatter, GitHub PUT, README gen | Week 3 |
| 4 — Popup UI | All 3 screens, animations, settings | Week 3–4 |
| 5 — Recovery | History import, deduplication, progress UI | Week 4–5 |
| 6 — Polish | Toasts, errors, Firefox pass | Week 5–6 |
| 7 — Launch | Tests, store submission | Week 6–7 |

---

## Legal Standing

- Only your **authored code** is committed — never problem content
- Uses official, public APIs within your own authenticated session
- GitHub OAuth is the standard, documented integration pattern
- Open-source proxy — fully auditable, self-hostable
- MIT licensed — clean for personal and commercial use

---

*leetie · Built by Sir · Planned by Luna*
