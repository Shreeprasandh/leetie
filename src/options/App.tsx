import React, { useEffect, useState } from 'react';
import { storage } from '../shared/storage';
import { ExtensionConfig } from '../shared/types';
import { Save, Check, Key, GitBranch, FolderTree } from 'lucide-react';

export default function OptionsApp() {
  const [config, setConfig] = useState<ExtensionConfig | null>(null);
  const [saved, setSaved] = useState(false);
  const [testing, setTesting] = useState(false);
  const [testResult, setTestResult] = useState<{ success: boolean; message: string } | null>(null);

  useEffect(() => {
    storage.getConfig().then(setConfig);
  }, []);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!config) return;
    await storage.setConfig(config);
    if (config.githubToken && config.githubUsername) {
      await storage.setState({ isAuthenticated: true });
    }
    setSaved(true);
    setTimeout(() => setSaved(false), 2000);
  };

  const handleTestConnection = async () => {
    if (!config?.githubToken) {
      setTestResult({ success: false, message: 'Please enter a GitHub Personal Access Token first.' });
      return;
    }
    setTesting(true);
    setTestResult(null);

    try {
      if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
        chrome.runtime.sendMessage(
          {
            type: 'TEST_CONNECTION',
            payload: { token: config.githubToken, username: config.githubUsername, repoName: config.repoName },
          },
          (res) => {
            setTesting(false);
            if (res?.success) {
              setTestResult({ success: true, message: `Connected as @${res.user.login}. Repository '${config.repoName}' verified.` });
            } else {
              setTestResult({ success: false, message: res?.error || 'Connection failed.' });
            }
          }
        );
      } else {
        // Local testing fallback
        const { GitHubService } = await import('../background/github.service');
        const user = await GitHubService.verifyUser(config.githubToken);
        setTesting(false);
        setTestResult({ success: true, message: `Connected as @${user.login}.` });
      }
    } catch (err: any) {
      setTesting(false);
      setTestResult({ success: false, message: err.message || 'Connection failed.' });
    }
  };

  if (!config) return null;

  return (
    <div style={{ maxWidth: 640, margin: '40px auto', padding: '0 20px' }}>
      <div style={{ marginBottom: 24 }}>
        <h1 style={{ fontSize: 24, fontWeight: 700, letterSpacing: '-0.02em' }}>leetie Settings</h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: 13, marginTop: 4 }}>
          Configure your GitHub connection, repository target, and commit formatting preferences.
        </p>
      </div>

      <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
        {/* GitHub Credentials */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <h2 style={{ fontSize: 16, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>
            <Key size={18} color="var(--primary)" /> GitHub Authorization
          </h2>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6 }}>
              Personal Access Token (or OAuth Token)
            </label>
            <input
              type="password"
              value={config.githubToken}
              onChange={(e) => setConfig({ ...config, githubToken: e.target.value })}
              placeholder="ghp_xxxxxxxxxxxxxxxxxxxx"
              style={{
                width: '100%',
                padding: '8px 12px',
                borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--border)',
                backgroundColor: 'var(--bg-primary)',
                color: 'var(--text-primary)',
                fontFamily: 'var(--font-mono)',
                fontSize: 13,
              }}
            />
            <span style={{ fontSize: 11, color: 'var(--text-muted)', display: 'block', marginTop: 4 }}>
              Requires <code>repo</code> scope to commit to your GitHub repository.
            </span>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6 }}>
              GitHub Username
            </label>
            <input
              type="text"
              value={config.githubUsername}
              onChange={(e) => setConfig({ ...config, githubUsername: e.target.value })}
              placeholder="e.g. octocat"
              style={{
                width: '100%',
                padding: '8px 12px',
                borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--border)',
                backgroundColor: 'var(--bg-primary)',
                color: 'var(--text-primary)',
                fontSize: 13,
              }}
            />
          </div>

          {testResult && (
            <div
              style={{
                padding: '10px 12px',
                borderRadius: 'var(--radius-sm)',
                fontSize: 12,
                backgroundColor: testResult.success ? 'rgba(34, 197, 94, 0.15)' : 'rgba(239, 68, 68, 0.15)',
                color: testResult.success ? '#4ade80' : '#f87171',
                border: `1px solid ${testResult.success ? 'rgba(34, 197, 94, 0.3)' : 'rgba(239, 68, 68, 0.3)'}`,
              }}
            >
              {testResult.message}
            </div>
          )}
        </div>

        {/* Repository Settings */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <h2 style={{ fontSize: 16, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>
            <GitBranch size={18} color="var(--primary)" /> Repository Configuration
          </h2>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6 }}>
                Repository Name
              </label>
              <input
                type="text"
                value={config.repoName}
                onChange={(e) => setConfig({ ...config, repoName: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  borderRadius: 'var(--radius-sm)',
                  border: '1px solid var(--border)',
                  backgroundColor: 'var(--bg-primary)',
                  color: 'var(--text-primary)',
                  fontSize: 13,
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6 }}>
                Branch Name
              </label>
              <input
                type="text"
                value={config.branch}
                onChange={(e) => setConfig({ ...config, branch: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  borderRadius: 'var(--radius-sm)',
                  border: '1px solid var(--border)',
                  backgroundColor: 'var(--bg-primary)',
                  color: 'var(--text-primary)',
                  fontSize: 13,
                }}
              />
            </div>
          </div>
        </div>

        {/* Sync Preferences */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <h2 style={{ fontSize: 16, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>
            <FolderTree size={18} color="var(--primary)" /> Commit Options
          </h2>

          <label style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer', fontSize: 13 }}>
            <input
              type="checkbox"
              checked={config.addHeaderComment}
              onChange={(e) => setConfig({ ...config, addHeaderComment: e.target.checked })}
            />
            Include metadata comment header in solution files (Problem link, difficulty, beats %)
          </label>

          <label style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer', fontSize: 13 }}>
            <input
              type="checkbox"
              checked={config.autoReadme}
              onChange={(e) => setConfig({ ...config, autoReadme: e.target.checked })}
            />
            Auto-generate and update <code>README.md</code> progress index in repository root
          </label>
        </div>

        {/* Action Buttons */}
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 12 }}>
          <button
            type="button"
            className="btn btn-secondary"
            onClick={handleTestConnection}
            disabled={testing}
            style={{ padding: '10px 16px' }}
          >
            {testing ? 'Testing...' : 'Test Connection'}
          </button>
          <button type="submit" className="btn btn-primary" style={{ padding: '10px 20px' }}>
            {saved ? <Check size={16} /> : <Save size={16} />}
            {saved ? 'Saved!' : 'Save Preferences'}
          </button>
        </div>
      </form>
    </div>
  );
}
