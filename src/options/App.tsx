import React, { useEffect, useState } from 'react';
import { storage } from '../shared/storage';
import { ExtensionConfig } from '../shared/types';
import { Save, Check, Key, GitBranch, FolderTree, Lock, Unlock, Edit3, X } from 'lucide-react';

export default function OptionsApp() {
  const [config, setConfig] = useState<ExtensionConfig | null>(null);
  const [initialConfig, setInitialConfig] = useState<ExtensionConfig | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [saved, setSaved] = useState(false);
  const [testing, setTesting] = useState(false);
  const [testResult, setTestResult] = useState<{ success: boolean; message: string } | null>(null);

  useEffect(() => {
    storage.getConfig().then((c) => {
      setConfig(c);
      setInitialConfig(c);
      // If token/username are empty, start in edit mode automatically
      if (!c.githubToken || !c.githubUsername) {
        setIsEditing(true);
      }
    });
  }, []);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!config) return;
    await storage.setConfig(config);
    setInitialConfig(config);
    if (config.githubToken && config.githubUsername) {
      await storage.setState({ isAuthenticated: true });
    }
    setIsEditing(false);
    setSaved(true);
    setTimeout(() => setSaved(false), 2500);
  };

  const handleCancel = () => {
    if (initialConfig) {
      setConfig({ ...initialConfig });
    }
    setIsEditing(false);
    setTestResult(null);
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
      {/* Top Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h1 style={{ fontSize: 24, fontWeight: 700, letterSpacing: '-0.02em', color: 'var(--text-primary)' }}>leetie Settings</h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: 13, marginTop: 4 }}>
            Configure your GitHub connection, repository target, and commit preferences.
          </p>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          {!isEditing ? (
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => setIsEditing(true)}
              style={{ display: 'flex', alignItems: 'center', gap: 6 }}
            >
              <Edit3 size={14} color="var(--sage-dark)" /> Edit Settings
            </button>
          ) : (
            <span style={{ fontSize: 11, color: 'var(--sage-dark)', fontWeight: 500, display: 'flex', alignItems: 'center', gap: 4 }}>
              <Unlock size={12} /> Editing Mode
            </span>
          )}
        </div>
      </div>

      {/* Lock Indicator Banner */}
      {!isEditing && (
        <div
          className="card"
          style={{
            marginBottom: 20,
            padding: '10px 14px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            fontSize: 12,
            backgroundColor: 'var(--bg-surface)',
            borderColor: 'var(--border)',
            color: 'var(--text-secondary)',
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <Lock size={14} color="var(--sage-dark)" />
            <span>Settings are locked to prevent accidental modifications.</span>
          </div>
          <button
            className="btn btn-secondary"
            style={{ padding: '4px 10px', fontSize: 11 }}
            onClick={() => setIsEditing(true)}
          >
            Unlock & Edit
          </button>
        </div>
      )}

      <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
        {/* GitHub Authorization */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16, opacity: isEditing ? 1 : 0.85 }}>
          <h2 style={{ fontSize: 15, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>
            <Key size={16} color="var(--sage-dark)" /> GitHub Authorization
          </h2>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6, color: 'var(--text-secondary)' }}>
              Personal Access Token (or OAuth Token)
            </label>
            <input
              type="password"
              value={config.githubToken}
              disabled={!isEditing}
              onChange={(e) => setConfig({ ...config, githubToken: e.target.value })}
              placeholder="ghp_xxxxxxxxxxxxxxxxxxxx"
              style={{
                width: '100%',
                padding: '8px 12px',
                borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--border)',
                backgroundColor: isEditing ? 'var(--bg-primary)' : 'var(--bg-surface-hover)',
                color: 'var(--text-primary)',
                fontFamily: 'var(--font-mono)',
                fontSize: 13,
                cursor: isEditing ? 'text' : 'not-allowed',
              }}
            />
            <span style={{ fontSize: 11, color: 'var(--text-muted)', display: 'block', marginTop: 4 }}>
              Requires <code>repo</code> scope to commit to your GitHub repository.
            </span>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6, color: 'var(--text-secondary)' }}>
              GitHub Username
            </label>
            <input
              type="text"
              value={config.githubUsername}
              disabled={!isEditing}
              onChange={(e) => setConfig({ ...config, githubUsername: e.target.value })}
              placeholder="e.g. Shreeprasandh"
              style={{
                width: '100%',
                padding: '8px 12px',
                borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--border)',
                backgroundColor: isEditing ? 'var(--bg-primary)' : 'var(--bg-surface-hover)',
                color: 'var(--text-primary)',
                fontSize: 13,
                cursor: isEditing ? 'text' : 'not-allowed',
              }}
            />
          </div>

          {testResult && (
            <div
              style={{
                padding: '10px 12px',
                borderRadius: 'var(--radius-sm)',
                fontSize: 12,
                backgroundColor: testResult.success ? 'rgba(162, 203, 139, 0.15)' : 'rgba(239, 68, 68, 0.15)',
                color: testResult.success ? 'var(--sage-main)' : '#f87171',
                border: `1px solid ${testResult.success ? 'rgba(162, 203, 139, 0.3)' : 'rgba(239, 68, 68, 0.3)'}`,
              }}
            >
              {testResult.message}
            </div>
          )}
        </div>

        {/* Repository Configuration */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16, opacity: isEditing ? 1 : 0.85 }}>
          <h2 style={{ fontSize: 15, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>
            <GitBranch size={16} color="var(--sage-dark)" /> Repository Configuration
          </h2>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6, color: 'var(--text-secondary)' }}>
                Repository Name
              </label>
              <input
                type="text"
                value={config.repoName}
                disabled={!isEditing}
                onChange={(e) => setConfig({ ...config, repoName: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  borderRadius: 'var(--radius-sm)',
                  border: '1px solid var(--border)',
                  backgroundColor: isEditing ? 'var(--bg-primary)' : 'var(--bg-surface-hover)',
                  color: 'var(--text-primary)',
                  fontSize: 13,
                  cursor: isEditing ? 'text' : 'not-allowed',
                }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6, color: 'var(--text-secondary)' }}>
                Branch Name
              </label>
              <input
                type="text"
                value={config.branch}
                disabled={!isEditing}
                onChange={(e) => setConfig({ ...config, branch: e.target.value })}
                style={{
                  width: '100%',
                  padding: '8px 12px',
                  borderRadius: 'var(--radius-sm)',
                  border: '1px solid var(--border)',
                  backgroundColor: isEditing ? 'var(--bg-primary)' : 'var(--bg-surface-hover)',
                  color: 'var(--text-primary)',
                  fontSize: 13,
                  cursor: isEditing ? 'text' : 'not-allowed',
                }}
              />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 6, color: 'var(--text-secondary)' }}>
              Solution Subfolder
            </label>
            <input
              type="text"
              value={config.solutionSubfolder}
              disabled={!isEditing}
              onChange={(e) => setConfig({ ...config, solutionSubfolder: e.target.value })}
              placeholder="solutions"
              style={{
                width: '100%',
                padding: '8px 12px',
                borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--border)',
                backgroundColor: isEditing ? 'var(--bg-primary)' : 'var(--bg-surface-hover)',
                color: 'var(--text-primary)',
                fontSize: 13,
                cursor: isEditing ? 'text' : 'not-allowed',
              }}
            />
          </div>
        </div>

        {/* Sync Preferences */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16, opacity: isEditing ? 1 : 0.85 }}>
          <h2 style={{ fontSize: 15, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>
            <FolderTree size={16} color="var(--sage-dark)" /> Commit Options
          </h2>

          <label style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: isEditing ? 'pointer' : 'not-allowed', fontSize: 13 }}>
            <input
              type="checkbox"
              disabled={!isEditing}
              checked={config.addHeaderComment}
              onChange={(e) => setConfig({ ...config, addHeaderComment: e.target.checked })}
            />
            Include metadata comment header in solution files (Problem link, difficulty, beats %)
          </label>

          <label style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: isEditing ? 'pointer' : 'not-allowed', fontSize: 13 }}>
            <input
              type="checkbox"
              disabled={!isEditing}
              checked={config.autoReadme}
              onChange={(e) => setConfig({ ...config, autoReadme: e.target.checked })}
            />
            Auto-generate and update <code>README.md</code> progress index in repository root
          </label>
        </div>

        {/* Legal & Terms Checkbox */}
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 11, color: 'var(--text-muted)', opacity: 0.75 }}>
          <input type="checkbox" id="terms" defaultChecked readOnly />
          <label htmlFor="terms">
            I accept the <a href="#privacy" onClick={() => alert('Privacy Policy: leetie operates 100% locally inside your browser. No data, tokens, or code are collected, transmitted, or sold to third parties.')} style={{ color: 'var(--text-secondary)' }}>Privacy Policy</a> and <a href="#terms" onClick={() => alert('Terms of Service: leetie is provided as-is under the MIT license. You retain full ownership of your code.')} style={{ color: 'var(--text-secondary)' }}>Terms of Service</a>.
          </label>
        </div>

        {/* Action Controls */}
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 12 }}>
          {isEditing ? (
            <>
              <button
                type="button"
                className="btn btn-secondary"
                onClick={handleCancel}
                style={{ padding: '10px 16px' }}
              >
                <X size={14} /> Cancel
              </button>
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
                <Save size={16} /> Save Changes
              </button>
            </>
          ) : (
            <button
              type="button"
              className="btn btn-primary"
              onClick={() => setIsEditing(true)}
              style={{ padding: '10px 20px' }}
            >
              {saved ? <Check size={16} /> : <Edit3 size={16} />}
              {saved ? 'Saved & Locked!' : 'Edit Settings'}
            </button>
          )}
        </div>

        {/* Copyright Footer */}
        <div style={{ marginTop: 24, textAlign: 'center', fontSize: 10, color: 'var(--text-muted)', opacity: 0.5 }}>
          © 2026 leetie · All rights reserved · Built for Sir
        </div>
      </form>
    </div>
  );
}
