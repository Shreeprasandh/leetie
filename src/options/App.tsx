import React, { useEffect, useState } from 'react';
import { storage } from '../shared/storage';
import { ExtensionConfig } from '../shared/types';
import { Save, Check, Key, GitBranch, FolderTree, Lock, Unlock, Edit3, X, Github, ChevronDown, ChevronUp, Info } from 'lucide-react';

export default function OptionsApp() {
  const [config, setConfig] = useState<ExtensionConfig | null>(null);
  const [initialConfig, setInitialConfig] = useState<ExtensionConfig | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [showManualPAT, setShowManualPAT] = useState(false);
  const [showPATGuide, setShowPATGuide] = useState(false);
  const [saved, setSaved] = useState(false);
  const [testing, setTesting] = useState(false);
  const [testResult, setTestResult] = useState<{ success: boolean; message: string } | null>(null);

  useEffect(() => {
    storage.getConfig().then((c) => {
      setConfig(c);
      setInitialConfig(c);
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
    setIsEditing(false);
    setSaved(true);
    setTimeout(() => setSaved(false), 2500);

    // If token is present, automatically test connection & set isAuthenticated: true
    if (config.githubToken) {
      if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
        chrome.runtime.sendMessage({
          type: 'TEST_CONNECTION',
          payload: { token: config.githubToken, username: config.githubUsername, repoName: config.repoName },
        }, (res) => {
          if (chrome.runtime.lastError) return;
          if (res?.success) {
            storage.getConfig().then((c) => { setConfig(c); setInitialConfig(c); });
          }
        });
      }
    }
  };

  const handleCancel = () => {
    if (initialConfig) {
      setConfig({ ...initialConfig });
      if (!initialConfig.githubToken) {
        setIsEditing(true);
      } else {
        setIsEditing(false);
      }
    } else {
      setIsEditing(true);
    }
    setTestResult(null);
  };

  const handleDisconnect = async () => {
    if (confirm('Are you sure you want to unlink your GitHub account from leetie?')) {
      await storage.setConfig({ githubToken: '', githubUsername: '' });
      await storage.setState({ isAuthenticated: false, recentCommits: [], totalSynced: 0, lastError: null });
      const updated = await storage.getConfig();
      setConfig(updated);
      setInitialConfig(updated);
      setIsEditing(true);
      setTestResult({ success: true, message: 'Successfully unlinked your GitHub account.' });
    }
  };

  const handle1ClickConnect = () => {
    setTesting(true);
    setTestResult(null);
    if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
      chrome.runtime.sendMessage({ type: 'START_OAUTH' }, (res) => {
        setTesting(false);
        if (chrome.runtime.lastError) {
          setTestResult({ success: false, message: chrome.runtime.lastError.message || 'OAuth authorization failed.' });
          return;
        }
        if (res?.success) {
          storage.getConfig().then((c) => {
            setConfig(c);
            setInitialConfig(c);
            setIsEditing(false); // Exit editing mode after successful OAuth
          });
          setTestResult({ success: true, message: `Successfully connected as @${res.user.login} via GitHub OAuth!` });
        } else {
          setTestResult({ success: false, message: res?.error || 'GitHub OAuth authorization failed.' });
        }
      });
    } else {
      setTesting(false);
      setTestResult({ success: false, message: 'OAuth requires extension runtime environment.' });
    }
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
            if (chrome.runtime.lastError) {
              setTestResult({ success: false, message: chrome.runtime.lastError.message || 'Connection failed.' });
              return;
            }
            if (res?.success) {
              // Reload config so the verified githubUsername reflects immediately in the UI
              storage.getConfig().then((c) => { setConfig(c); setInitialConfig(c); });
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
      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 20 }}>
        <div>
          <h1 style={{ fontSize: 24, fontWeight: 700, letterSpacing: '-0.02em', color: 'var(--text-primary)' }}>leetie Settings</h1>
          <p style={{ color: 'var(--text-secondary)', fontSize: 13, marginTop: 4 }}>
            Configure your GitHub connection, repository target, and commit preferences.
          </p>
        </div>
        <span style={{ fontSize: 11, color: isEditing ? 'var(--sage-dark)' : 'var(--text-muted)', fontWeight: 500, display: 'flex', alignItems: 'center', gap: 6 }}>
          {isEditing ? <Unlock size={14} /> : <Lock size={14} />}
          {isEditing ? 'Editing Mode' : 'Settings Locked'}
        </span>
      </div>

      <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
        {/* GitHub Connection Card */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16, opacity: isEditing ? 1 : 0.9 }}>
          <h2 style={{ fontSize: 15, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>
            <Github size={18} color="var(--sage-dark)" /> GitHub Authorization
          </h2>

          {/* Primary Option: 1-Click OAuth (Recommended) */}
          <div
            style={{
              padding: 16,
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--border)',
              backgroundColor: 'var(--bg-primary)',
              display: 'flex',
              flexDirection: 'column',
              gap: 10,
            }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div>
                <span style={{ fontSize: 13, fontWeight: 600 }}>Primary Method: 1-Click OAuth</span>
                <span className="badge badge-easy" style={{ marginLeft: 8 }}>Recommended</span>
              </div>
              {config.githubUsername && (
                <span style={{ fontSize: 12, color: 'var(--sage-main)', fontWeight: 500 }}>
                  Connected as @{config.githubUsername}
                </span>
              )}
            </div>
            <p style={{ fontSize: 12, color: 'var(--text-secondary)' }}>
              No need to generate or paste keys manually. Click below to securely connect your GitHub account in 1 click.
            </p>
            <div style={{ display: 'flex', gap: 10, alignItems: 'center' }}>
              <button
                type="button"
                className="btn btn-primary"
                onClick={handle1ClickConnect}
                disabled={!isEditing || testing}
                style={{ alignSelf: 'flex-start', padding: '8px 16px', opacity: !isEditing ? 0.7 : 1 }}
              >
                <Github size={16} /> {testing ? 'Connecting...' : '1-Click Connect GitHub'}
              </button>
              {config.githubUsername && (
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={handleDisconnect}
                  style={{ padding: '8px 14px', color: '#f87171', fontSize: 12 }}
                >
                  Unlink Account
                </button>
              )}
            </div>
          </div>

          {/* Secondary Option: Manual Personal Access Token (PAT) */}
          <div style={{ marginTop: 4 }}>
            <button
              type="button"
              onClick={() => setShowManualPAT(!showManualPAT)}
              style={{
                background: 'none',
                border: 'none',
                color: 'var(--text-secondary)',
                fontSize: 12,
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: 6,
                padding: 0,
              }}
            >
              <Key size={14} color="var(--sage-dark)" />
              <span>Secondary Option: Manual Personal Access Token (Advanced / Fallback)</span>
              {showManualPAT ? <ChevronUp size={14} /> : <ChevronDown size={14} />}
            </button>

            {showManualPAT && (
              <div style={{ marginTop: 12, display: 'flex', flexDirection: 'column', gap: 12, paddingLeft: 12, borderLeft: '2px solid var(--border)' }}>
                <p style={{ fontSize: 11, color: 'var(--text-muted)' }}>
                  If you prefer not to use 1-click OAuth or have a custom GitHub instance, manually enter your Personal Access Token below:
                </p>
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 4 }}>
                    <label style={{ fontSize: 12, fontWeight: 500, color: 'var(--text-secondary)' }}>
                      Personal Access Token
                    </label>
                    <button
                      type="button"
                      onClick={() => setShowPATGuide(!showPATGuide)}
                      title="How to get a GitHub Personal Access Token"
                      style={{
                        background: 'none',
                        border: 'none',
                        color: 'var(--sage-dark)',
                        cursor: 'pointer',
                        display: 'inline-flex',
                        alignItems: 'center',
                        padding: 0,
                      }}
                    >
                      <Info size={14} />
                    </button>
                  </div>

                  {showPATGuide && (
                    <div
                      style={{
                        marginBottom: 10,
                        padding: 12,
                        borderRadius: 'var(--radius-sm)',
                        backgroundColor: 'var(--bg-surface-hover)',
                        border: '1px solid var(--border)',
                        fontSize: 11,
                        lineHeight: 1.6,
                        color: 'var(--text-secondary)',
                      }}
                    >
                      <strong style={{ color: 'var(--sage-main)', display: 'block', marginBottom: 4 }}>
                        How to get a GitHub Personal Access Token (PAT):
                      </strong>
                      <ol style={{ paddingLeft: 16, margin: 0 }}>
                        <li>Go to <a href="https://github.com/settings/tokens" target="_blank" rel="noreferrer" style={{ color: 'var(--sage-light)', textDecoration: 'underline' }}>github.com/settings/tokens</a>.</li>
                        <li>Click <strong>Generate new token (classic)</strong>.</li>
                        <li>Add a note (e.g., <code>leetie extension</code>).</li>
                        <li>Check the <strong><code>repo</code></strong> scope box.</li>
                        <li>Click <strong>Generate token</strong> at the bottom of the page.</li>
                        <li>Copy your generated token string (starts with <code>ghp_</code>) and paste it below.</li>
                      </ol>
                    </div>
                  )}

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
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: 12, fontWeight: 500, marginBottom: 4, color: 'var(--text-secondary)' }}>
                    GitHub Username
                  </label>
                  <input
                    type="text"
                    value={config.githubUsername}
                    disabled={!isEditing}
                    onChange={(e) => setConfig({ ...config, githubUsername: e.target.value })}
                    placeholder="e.g. your-github-username"
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
            )}
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
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16, opacity: isEditing ? 1 : 0.9 }}>
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
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16, opacity: isEditing ? 1 : 0.9 }}>
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
            I accept the <a href="#privacy" onClick={() => alert('Privacy Policy: leetie operates 100% locally inside your browser. No data, tokens, or code are collected, transmitted, or sold to third parties.')} style={{ color: 'var(--text-secondary)' }}>Privacy Policy</a> and <a href="#terms" onClick={() => alert('Terms of Service: leetie is provided as-is under the GNU General Public License v3 (GPL-3.0). You retain full ownership of your code.')} style={{ color: 'var(--text-secondary)' }}>Terms of Service (GPLv3)</a>.
          </label>
        </div>

        {/* Action Controls — Single Primary Control Row */}
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
              {showManualPAT && (
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={handleTestConnection}
                  disabled={testing}
                  style={{ padding: '10px 16px' }}
                >
                  {testing ? 'Testing...' : 'Test Manual Token'}
                </button>
              )}
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
      </form>

      {/* Author & Project Signature */}
      <div
        style={{
          marginTop: 32,
          paddingTop: 16,
          borderTop: '1px solid var(--border)',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          fontSize: 11,
          color: 'var(--text-muted)',
        }}
      >
        <span>leetie v1.0.0 · Licensed under GPL-3.0</span>
        <span>
          Built with care by{' '}
          <a
            href="https://github.com/Shreeprasandh"
            target="_blank"
            rel="noreferrer"
            style={{ color: 'var(--sage-main)', textDecoration: 'none', fontWeight: 500 }}
          >
            Shreeprasandh K
          </a>
        </span>
      </div>
    </div>
  );
}
