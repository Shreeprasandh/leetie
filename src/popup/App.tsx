import { useEffect, useState } from 'react';
import { storage } from '../shared/storage';
import { ExtensionConfig, ExtensionState } from '../shared/types';
import { Github, Settings, CheckCircle, RefreshCw, ExternalLink, AlertCircle } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

function formatRelativeTime(timestamp: number): string {
  const diffSec = Math.floor((Date.now() - timestamp) / 1000);
  if (diffSec < 60) return 'Just now';
  if (diffSec < 3600) return `${Math.floor(diffSec / 60)}m ago`;
  if (diffSec < 86400) return `${Math.floor(diffSec / 3600)}h ago`;
  return `${Math.floor(diffSec / 86400)}d ago`;
}

export default function App() {
  const [config, setConfig] = useState<ExtensionConfig | null>(null);
  const [state, setState] = useState<ExtensionState | null>(null);

  const loadData = async () => {
    const c = await storage.getConfig();
    const s = await storage.getState();
    setConfig(c);
    setState(s);
  };

  useEffect(() => {
    loadData();
    const interval = setInterval(loadData, 2000);
    return () => clearInterval(interval);
  }, []);

  const openOptions = () => {
    if (typeof chrome !== 'undefined' && chrome.runtime?.openOptionsPage) {
      chrome.runtime.openOptionsPage();
    } else {
      window.open('/src/options/index.html', '_blank');
    }
  };

  if (!config || !state) {
    return (
      <div style={{ width: 360, height: 440, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <RefreshCw className="animate-spin" size={20} color="var(--primary)" />
      </div>
    );
  }

  const getStatusColor = () => {
    if (state.syncStatus === 'syncing') return 'var(--warning-amber)';
    if (state.syncStatus === 'error') return 'var(--error-red)';
    return state.isAuthenticated ? 'var(--accent-green)' : 'var(--text-muted)';
  };

  const getStatusLabel = () => {
    if (state.syncStatus === 'syncing') return 'Syncing...';
    if (state.syncStatus === 'error') return 'Error';
    return state.isAuthenticated ? 'Live' : 'Offline';
  };

  const handleStartRecovery = () => {
    if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
      chrome.runtime.sendMessage({ type: 'RECOVERY_START' });
    }
  };

  const handleStopRecovery = () => {
    if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
      chrome.runtime.sendMessage({ type: 'RECOVERY_STOP' });
    }
  };

  const handleConnectGitHub = () => {
    if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
      chrome.runtime.sendMessage({ type: 'START_OAUTH' }, (res) => {
        if (res?.success) {
          loadData();
        }
      });
    } else {
      openOptions();
    }
  };

  return (
    <div style={{ width: 360, minHeight: 460, padding: 16, display: 'flex', flexDirection: 'column', gap: 16 }}>
      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <h1 style={{ fontSize: 18, fontWeight: 700, letterSpacing: '-0.02em', color: 'var(--text-primary)' }}>leetie</h1>
          <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>v1.0.0</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 11, fontWeight: 500, color: 'var(--text-secondary)' }}>
            <motion.div
              animate={state.syncStatus === 'syncing' || state.syncStatus === 'recovering' ? { scale: [1, 1.3, 1] } : {}}
              transition={{ repeat: Infinity, duration: 1 }}
              style={{
                width: 8,
                height: 8,
                borderRadius: '50%',
                backgroundColor: getStatusColor(),
              }}
            />
            {getStatusLabel()}
          </div>
          <button className="btn btn-secondary" style={{ padding: 6 }} onClick={openOptions} title="Settings">
            <Settings size={14} />
          </button>
        </div>
      </div>

      {/* Error Banner */}
      {state.lastError && (
        <div
          className="card"
          style={{
            padding: 10,
            display: 'flex',
            alignItems: 'center',
            gap: 8,
            backgroundColor: 'rgba(239, 68, 68, 0.15)',
            borderColor: 'rgba(239, 68, 68, 0.3)',
            color: '#f87171',
            fontSize: 12,
          }}
        >
          <AlertCircle size={16} style={{ flexShrink: 0 }} />
          <span style={{ flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
            {state.lastError}
          </span>
        </div>
      )}

      {/* Auth Card / Active Connection Card */}
      {!state.isAuthenticated ? (
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 12, textAlign: 'center' }}>
          <Github size={32} style={{ margin: '0 auto', color: 'var(--primary)' }} />
          <div>
            <h3 style={{ fontSize: 15, fontWeight: 600 }}>Connect GitHub</h3>
            <p style={{ fontSize: 12, color: 'var(--text-secondary)', marginTop: 4 }}>
              Auto-archive your accepted LeetCode solutions directly to your repository.
            </p>
          </div>
          <button className="btn btn-primary" onClick={handleConnectGitHub}>
            <Github size={16} /> 1-Click Connect GitHub
          </button>
          <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>100% client-side. Data stays in your browser.</span>
        </div>
      ) : (
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <span style={{ fontSize: 12, color: 'var(--text-secondary)' }}>Target Repository</span>
            <span className="badge badge-easy">Active</span>
          </div>
          <div style={{ fontSize: 13, fontWeight: 600, fontFamily: 'var(--font-mono)' }}>
            {config.githubUsername || 'user'}/{config.repoName}
          </div>

          {state.syncStatus === 'recovering' && state.recoveryProgress ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6, marginTop: 4 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, color: 'var(--text-secondary)' }}>
                <span>Recovering History...</span>
                <span>{state.recoveryProgress.current} / {state.recoveryProgress.total}</span>
              </div>
              <div style={{ width: '100%', height: 6, backgroundColor: 'var(--bg-primary)', borderRadius: 3, overflow: 'hidden' }}>
                <div
                  style={{
                    width: `${Math.min(100, Math.round((state.recoveryProgress.current / (state.recoveryProgress.total || 1)) * 100))}%`,
                    height: '100%',
                    backgroundColor: 'var(--primary)',
                    transition: 'width 0.3s ease',
                  }}
                />
              </div>
              <button className="btn btn-secondary" style={{ marginTop: 4, padding: '4px 10px', fontSize: 11 }} onClick={handleStopRecovery}>
                Stop Recovery
              </button>
            </div>
          ) : (
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 4 }}>
              <div style={{ fontSize: 11, color: 'var(--text-muted)' }}>
                Subfolder: <code>{config.solutionSubfolder || 'solutions'}</code>
              </div>
              <button className="btn btn-secondary" style={{ padding: '4px 10px', fontSize: 11 }} onClick={handleStartRecovery}>
                <RefreshCw size={12} /> Recover History
              </button>
            </div>
          )}
        </div>
      )}

      {/* Recent Activity List */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <h2 style={{ fontSize: 13, fontWeight: 600, color: 'var(--text-secondary)' }}>Recent Commits</h2>
          <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>{state.recentCommits.length} total synced</span>
        </div>

        {state.recentCommits.length === 0 ? (
          <div
            className="card"
            style={{
              padding: 24,
              textAlign: 'center',
              color: 'var(--text-muted)',
              fontSize: 12,
            }}
          >
            No solutions synced yet. Solve a problem on LeetCode to trigger auto-sync!
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            <AnimatePresence>
              {state.recentCommits.slice(0, 4).map((item) => (
                <motion.div
                  key={item.submissionId}
                  initial={{ opacity: 0, y: 6 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0 }}
                  className="card"
                  style={{
                    padding: '10px 12px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    fontSize: 12,
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: 8, overflow: 'hidden' }}>
                    <CheckCircle size={14} color="var(--accent-green)" style={{ flexShrink: 0 }} />
                    <div style={{ display: 'flex', flexDirection: 'column' }}>
                      <span style={{ fontWeight: 500, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                        {item.problemTitle}
                      </span>
                      <span style={{ fontSize: 10, color: 'var(--text-muted)' }}>
                        {item.lang} · {formatRelativeTime(item.committedAt)}
                      </span>
                    </div>
                  </div>
                  <span className={`badge badge-${item.difficulty.toLowerCase()}`}>{item.difficulty}</span>
                </motion.div>
              ))}
            </AnimatePresence>
          </div>
        )}
      </div>

      {/* Footer */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4, alignItems: 'center', fontSize: 10, color: 'var(--text-muted)', opacity: 0.5, marginTop: 8 }}>
        <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          <span>© 2026 leetie</span>
          <span>·</span>
          <a
            href="#privacy"
            onClick={() => openOptions()}
            style={{ color: 'var(--text-muted)', textDecoration: 'underline' }}
          >
            Privacy Policy
          </a>
          <span>·</span>
          <a
            href="https://leetcode.com"
            target="_blank"
            rel="noreferrer"
            style={{ color: 'var(--text-muted)', textDecoration: 'none', display: 'flex', alignItems: 'center', gap: 2 }}
          >
            LeetCode <ExternalLink size={9} />
          </a>
        </div>
      </div>
    </div>
  );
}
