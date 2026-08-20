# Privacy Policy for leetie

**Last updated:** August 20, 2026

**Author:** Shreeprasandh K (<https://github.com/Shreeprasandh/leetie>)

---

### Overview
**leetie** is a client-side browser extension designed to automatically sync your accepted LeetCode solutions to your personal GitHub repository. 

Your privacy and security are our highest priority. **leetie does not collect, track, or store any personal data on external servers.**

---

### 1. Data Collection and Usage
* **No Remote Servers**: leetie operates 100% locally within your browser. There are no tracking analytics, external telemetry, or centralized databases.
* **Authentication Tokens**: Your GitHub authentication tokens (OAuth access tokens or Personal Access Tokens) are stored strictly on your local machine using the sandboxed `chrome.storage.local` API. They are never sent to any server other than the official GitHub API (`api.github.com`).
* **LeetCode Solutions**: The code you write and submit on LeetCode is transmitted solely to the GitHub repository you specify under your authenticated session.
* **OAuth Token Exchange Proxy**: If you choose the 1-Click GitHub OAuth method, the temporary authorization code is exchanged via an open-source, stateless serverless proxy. The proxy never logs, stores, or inspects your access tokens.

---

### 2. Permissions Justification
* `storage`: Required to save your repository preferences and GitHub authentication token locally in your browser.
* `identity`: Required to perform the standard, secure 1-click GitHub OAuth authentication flow.
* `tabs` & `scripting`: Required to detect accepted submissions in your active LeetCode session and retrieve solution metadata.
* `notifications`: Required to display brief desktop notifications confirming when a solution is committed.
* `host_permissions` (`leetcode.com`, `api.github.com`): Required to intercept submission responses and commit solution files directly to your GitHub repository.

---

### 3. Third-Party Sharing
leetie does **not** sell, rent, or trade your personal information or code to any third party.

---

### 4. Open Source & Auditability
leetie is open-source under the **GNU General Public License v3 (GPL-3.0)**. The complete source code is public and auditable at:  
<https://github.com/Shreeprasandh/leetie>

---

### 5. Contact
For questions or feedback regarding this Privacy Policy, please open an issue on GitHub or contact Shreeprasandh K via <https://github.com/Shreeprasandh>.
