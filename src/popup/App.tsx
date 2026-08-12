import { useEffect, useState } from 'react';
import { storage } from '../shared/storage';
import { ExtensionConfig, ExtensionState } from '../shared/types';
import { Github, Settings, CheckCircle, RefreshCw, ExternalLink } from 'lucide-react';

export default function App() {
  const [config, setConfig] = useState<ExtensionConfig | null>(null);
  const [state, setState] = useState<ExtensionState | null>(null);

  useEffect(() => {
    async function loadData() {
      const c = await storage.getConfig();
      const s = await storage.getState();
      setConfig(c);
      setState(s);
    }
    loadData();
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
        <RefreshCw className="animate-spin" size={20} />
      </div>
    );
  }

  return (
    <div style={{ width: 360, minHeight: 440, padding: 16, display: 'flex', flexDirection: 'column', gap: 16 }}>
      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <h1 style={{ fontSize: 18, fontWeight: 700, letterSpacing: '-0.02em' }}>leetie</h1>
          <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>v1.0.0</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <div
            style={{
              width: 8,
              height: 8,
              borderRadius: '50%',
              backgroundColor: state.isAuthenticated ? 'var(--accent-green)' : 'var(--text-muted)',
            }}
          />
          <button className="btn btn-secondary" style={{ padding: 6 }} onClick={openOptions} title="Settings">
            <Settings size={14} />
          </button>
        </div>
      </div>

      {/* Auth Card / Active Card */}
      {!state.isAuthenticated ? (
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 12, textAlign: 'center' }}>
          <Github size={32} style={{ margin: '0 auto', color: 'var(--primary)' }} />
          <div>
            <h3 style={{ fontSize: 15, fontWeight: 600 }}>Connect GitHub</h3>
            <p style={{ fontSize: 12, color: 'var(--text-secondary)', marginTop: 4 }}>
              Auto-archive your accepted LeetCode solutions directly to your repository.
            </p>
          </div>
          <button className="btn btn-primary" onClick={openOptions}>
            <Github size={16} /> Configure Connection
          </button>
          <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>100% client-side. Data stays in your browser.</span>
        </div>
      ) : (
        <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <span style={{ fontSize: 12, color: 'var(--text-secondary)' }}>Target Repository</span>
            <span className="badge badge-easy">Connected</span>
          </div>
          <div style={{ fontSize: 13, fontWeight: 600, fontFamily: 'var(--font-mono)' }}>
            {config.githubUsername || 'user'}/{config.repoName} ({config.branch})
          </div>
        </div>
      )}

      {/* Recent Activity */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <h2 style={{ fontSize: 13, fontWeight: 600, color: 'var(--text-secondary)' }}>Recent Commits</h2>
          <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>{state.recentCommits.length} synced</span>
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
            No solutions synced yet. Solve a problem on LeetCode to test!
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {state.recentCommits.slice(0, 4).map((item) => (
              <div
                key={item.submissionId}
                className="card"
                style={{
                  padding: '10px 12px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  fontSize: 12,
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <CheckCircle size={14} color="var(--accent-green)" />
                  <span style={{ fontWeight: 500 }}>{item.problemTitle}</span>
                </div>
                <span className={`badge badge-${item.difficulty.toLowerCase()}`}>{item.difficulty}</span>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Footer */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 11, color: 'var(--text-muted)' }}>
        <span>leetie by Sir</span>
        <a
          href="https://leetcode.com"
          target="_blank"
          rel="noreferrer"
          style={{ color: 'var(--text-muted)', textDecoration: 'none', display: 'flex', alignItems: 'center', gap: 4 }}
        >
          LeetCode <ExternalLink size={10} />
        </a>
      </div>
    </div>
  );
}
