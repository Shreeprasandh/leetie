import { useEffect, useRef, useState } from 'react';
import { storage } from '../shared/storage';
import { ExtensionConfig, ExtensionState } from '../shared/types';
import { Github, Settings, CheckCircle, RefreshCw, ExternalLink, AlertCircle, Target, TrendingUp, Database } from 'lucide-react';
import { motion } from 'framer-motion';

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
  const [isSyncing, setIsSyncing] = useState(false);

  const loadData = async () => {
    const c = await storage.getConfig();
    const s = await storage.getState();
    setConfig(c);
    setState(s);
  };

  const debounceRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    loadData();
    const onChange = () => {
      if (debounceRef.current) clearTimeout(debounceRef.current);
      debounceRef.current = setTimeout(() => {
        debounceRef.current = null;
        loadData();
      }, 100);
    };
    if (typeof chrome !== 'undefined' && chrome.storage?.onChanged) {
      chrome.storage.onChanged.addListener(onChange);
      return () => {
        chrome.storage.onChanged.removeListener(onChange);
        if (debounceRef.current) clearTimeout(debounceRef.current);
      };
    }
    return undefined;
  }, []);

  const openOptions = () => {
    if (typeof chrome !== 'undefined' && chrome.runtime?.openOptionsPage) {
      chrome.runtime.openOptionsPage();
    } else {
      window.open('/options/index.html', '_blank');
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

  const handleConnectGitHub = () => {
    if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
      chrome.runtime.sendMessage({ type: 'START_OAUTH' }, (res) => {
        if (chrome.runtime.lastError) {
          console.warn('[leetie] OAuth could not reach background SW:', chrome.runtime.lastError.message);
          return;
        }
        if (res?.success) {
          loadData();
        }
      });
    } else {
      openOptions();
    }
  };

  const handleDisconnect = async () => {
    await storage.setConfig({ githubToken: '', githubUsername: '' });
    await storage.setState({ isAuthenticated: false, recentCommits: [], totalSynced: 0, lastError: null });
    loadData();
  };

  const handleSyncFromGitHub = () => {
    setIsSyncing(true);
    if (typeof chrome !== 'undefined' && chrome.runtime?.sendMessage) {
      chrome.runtime.sendMessage({ type: 'SYNC_GITHUB' }, (res) => {
        setIsSyncing(false);
        if (chrome.runtime.lastError) {
          console.warn('[leetie] GitHub sync error:', chrome.runtime.lastError.message);
        } else if (res?.success) {
          loadData();
        }
      });
    } else {
      setIsSyncing(false);
    }
  };

  const stats = state.syncedStats || {
    easySolved: 0,
    mediumSolved: 0,
    hardSolved: 0,
    totalSolved: state.totalSynced || 0,
    totalSubmissions: state.recentCommits.length,
    acceptanceRate: 0,
    streak: 0,
  };

  const latestCommit = state.recentCommits && state.recentCommits.length > 0 ? state.recentCommits[0] : null;

  return (
    <div style={{ width: 360, minHeight: 480, padding: 16, display: 'flex', flexDirection: 'column', gap: 14 }}>
      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <h1 style={{ fontSize: 18, fontWeight: 700, letterSpacing: '-0.02em', color: 'var(--text-primary)' }}>leetie</h1>
          <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>v1.0.0</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 11, fontWeight: 500, color: 'var(--text-secondary)' }}>
            <motion.div
              animate={state.syncStatus === 'syncing' ? { scale: [1, 1.3, 1] } : {}}
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
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <span style={{ fontSize: 12, color: 'var(--text-secondary)' }}>Target Repository</span>
            <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
              <span className="badge badge-easy">Active</span>
              <button
                className="btn btn-secondary"
                style={{ padding: '2px 8px', fontSize: 10, color: '#f87171', border: 'none', background: 'none', cursor: 'pointer' }}
                onClick={handleDisconnect}
                title="Unlink GitHub Account"
              >
                Unlink
              </button>
            </div>
          </div>
          <div style={{ fontSize: 13, fontWeight: 600, fontFamily: 'var(--font-mono)' }}>
            {config.githubUsername || 'user'}/{config.repoName}
          </div>

          <div style={{ fontSize: 11, color: 'var(--text-muted)', marginTop: 2 }}>
            Subfolder: <code>{config.solutionSubfolder || 'solutions'}</code>
          </div>
        </div>
      )}

      {/* LeetCode Performance Overview Card */}
      {state.isAuthenticated && (
        <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 10 }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <h2 style={{ fontSize: 13, fontWeight: 600, color: 'var(--text-secondary)', display: 'flex', alignItems: 'center', gap: 6 }}>
              <Target size={14} color="var(--primary)" /> Overview
            </h2>
            <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
              <span style={{ fontSize: 10, color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: 4 }}>
                <Database size={10} /> Git Synced Archive
              </span>
              <button
                type="button"
                className="btn btn-secondary"
                style={{ padding: '2px 6px', fontSize: 10, display: 'flex', alignItems: 'center', gap: 4 }}
                onClick={handleSyncFromGitHub}
                disabled={isSyncing}
                title="Fetch stats from your GitHub repository"
              >
                <RefreshCw size={10} className={isSyncing ? 'animate-spin' : ''} />
                {isSyncing ? 'Syncing...' : 'Sync'}
              </button>
            </div>
          </div>

          {/* 2 Primary Stats Cards: Solved & Submissions */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8 }}>
            <div className="card" style={{ padding: '10px 12px', display: 'flex', flexDirection: 'column', gap: 2 }}>
              <span style={{ fontSize: 10, color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: 4 }}>
                <Target size={12} color="var(--accent-green)" /> Solved
              </span>
              <span style={{ fontSize: 20, fontWeight: 700, fontFamily: 'var(--font-mono)' }}>{stats.totalSolved}</span>
            </div>

            <div className="card" style={{ padding: '10px 12px', display: 'flex', flexDirection: 'column', gap: 2 }}>
              <span style={{ fontSize: 10, color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: 4 }}>
                <TrendingUp size={12} color="var(--primary)" /> Submissions
              </span>
              <span style={{ fontSize: 20, fontWeight: 700, fontFamily: 'var(--font-mono)' }}>{stats.totalSubmissions}</span>
            </div>
          </div>

          {/* Easy / Medium / Hard Individual Progress Bars */}
          <div className="card" style={{ padding: 12, display: 'flex', flexDirection: 'column', gap: 10 }}>
            <span style={{ fontSize: 11, fontWeight: 600, color: 'var(--text-secondary)' }}>Difficulty Breakdown</span>
            
            {/* Easy Row */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, fontWeight: 500 }}>
                <span style={{ color: 'var(--accent-green)' }}>Easy</span>
                <span style={{ color: 'var(--text-primary)', fontFamily: 'var(--font-mono)' }}>{stats.easySolved}</span>
              </div>
              <div style={{ width: '100%', height: 6, backgroundColor: 'var(--bg-primary)', borderRadius: 3, overflow: 'hidden' }}>
                <div
                  style={{
                    width: `${stats.totalSolved > 0 ? (stats.easySolved / stats.totalSolved) * 100 : 0}%`,
                    height: '100%',
                    backgroundColor: 'var(--accent-green)',
                    transition: 'width 0.3s ease',
                  }}
                />
              </div>
            </div>

            {/* Medium Row */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, fontWeight: 500 }}>
                <span style={{ color: 'var(--warning-amber)' }}>Medium</span>
                <span style={{ color: 'var(--text-primary)', fontFamily: 'var(--font-mono)' }}>{stats.mediumSolved}</span>
              </div>
              <div style={{ width: '100%', height: 6, backgroundColor: 'var(--bg-primary)', borderRadius: 3, overflow: 'hidden' }}>
                <div
                  style={{
                    width: `${stats.totalSolved > 0 ? (stats.mediumSolved / stats.totalSolved) * 100 : 0}%`,
                    height: '100%',
                    backgroundColor: 'var(--warning-amber)',
                    transition: 'width 0.3s ease',
                  }}
                />
              </div>
            </div>

            {/* Hard Row */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, fontWeight: 500 }}>
                <span style={{ color: '#f87171' }}>Hard</span>
                <span style={{ color: 'var(--text-primary)', fontFamily: 'var(--font-mono)' }}>{stats.hardSolved}</span>
              </div>
              <div style={{ width: '100%', height: 6, backgroundColor: 'var(--bg-primary)', borderRadius: 3, overflow: 'hidden' }}>
                <div
                  style={{
                    width: `${stats.totalSolved > 0 ? (stats.hardSolved / stats.totalSolved) * 100 : 0}%`,
                    height: '100%',
                    backgroundColor: '#f87171',
                    transition: 'width 0.3s ease',
                  }}
                />
              </div>
            </div>
          </div>

          {/* Latest Sync Ticker */}
          {latestCommit && (
            <div className="card" style={{ padding: '8px 10px', display: 'flex', alignItems: 'center', gap: 8, fontSize: 11 }}>
              <CheckCircle size={12} color="var(--accent-green)" style={{ flexShrink: 0 }} />
              <span style={{ color: 'var(--text-muted)', flexShrink: 0 }}>Latest:</span>
              <span style={{ fontWeight: 500, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', flex: 1 }}>
                {latestCommit.problemTitle}
              </span>
              <span style={{ fontSize: 10, color: 'var(--text-muted)', flexShrink: 0 }}>{formatRelativeTime(latestCommit.committedAt)}</span>
            </div>
          )}

          {/* Footnote requested by user */}
          <div style={{ fontSize: 9, color: 'var(--text-muted)', textAlign: 'center', opacity: 0.6 }}>
            * Data based on synced Git storage, not live LeetCode account.
          </div>
        </div>
      )}

      {/* Footer */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4, alignItems: 'center', fontSize: 10, color: 'var(--text-muted)', opacity: 0.5, marginTop: 'auto' }}>
        <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
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
