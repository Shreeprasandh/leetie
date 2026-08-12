import React, { useEffect, useState } from 'react';
import { storage } from '../shared/storage';
import { ExtensionConfig } from '../shared/types';
import { Save, Check, Key, GitBranch, FolderTree } from 'lucide-react';

export default function OptionsApp() {
  const [config, setConfig] = useState<ExtensionConfig | null>(null);
  const [saved, setSaved] = useState(false);

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

        {/* Save Button */}
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 12 }}>
          <button type="submit" className="btn btn-primary" style={{ padding: '10px 20px' }}>
            {saved ? <Check size={16} /> : <Save size={16} />}
            {saved ? 'Saved!' : 'Save Preferences'}
          </button>
        </div>
      </form>
    </div>
  );
}
