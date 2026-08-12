import { defineConfig, Plugin } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve, dirname } from 'path';
import { copyFileSync, mkdirSync, existsSync, readFileSync, writeFileSync, rmSync } from 'fs';

function copyManifestPlugin() {
  return {
    name: 'copy-manifest',
    closeBundle() {
      if (!existsSync('dist')) {
        mkdirSync('dist', { recursive: true });
      }
      copyFileSync('manifest.json', 'dist/manifest.json');
    },
  };
}

// Vite preserves the src/ directory structure in the output, which produces
// dist/src/popup/index.html instead of the flat dist/popup/index.html that
// Chrome extensions expect. This plugin copies the HTML files to the correct
// flat locations after the build is written to disk.
function flattenHtmlPlugin(): Plugin {
  return {
    name: 'flatten-html',
    closeBundle() {
      const moves: [string, string][] = [
        ['dist/src/popup/index.html', 'dist/popup/index.html'],
        ['dist/src/options/index.html', 'dist/options/index.html'],
      ];

      for (const [src, dest] of moves) {
        try {
          mkdirSync(dirname(dest), { recursive: true });
          // Rewrite relative asset paths: Vite writes ../../assets/ relative to
          // dist/src/popup/ (two levels up). After copying to dist/popup/ (one
          // level up), all references must become ../assets/ instead.
          const html = readFileSync(src, 'utf-8').replace(/\.\.\/\.\.\/assets\//g, '../assets/');
          writeFileSync(dest, html);
        } catch (e) {
          console.warn(`[flatten-html] Could not copy ${src} → ${dest}:`, e);
        }
      }

      // Clean up the now-redundant src/ tree in dist/
      try {
        rmSync('dist/src', { recursive: true, force: true });
      } catch (_) { /* ignore */ }
    },
  };
}

export default defineConfig({
  base: '', // CRITICAL: forces relative asset paths in built HTML.
  // Chrome extensions cannot resolve absolute /assets/ paths.
  plugins: [react(), flattenHtmlPlugin(), copyManifestPlugin()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
  build: {
    outDir: 'dist',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        popup: resolve(__dirname, 'src/popup/index.html'),
        options: resolve(__dirname, 'src/options/index.html'),
        background: resolve(__dirname, 'src/background/index.ts'),
        content: resolve(__dirname, 'src/content/index.ts'),
        // injected runs in MAIN world — separate entry so Chrome can load it
        // via content_scripts[world=MAIN] before the page's own scripts run
        injected: resolve(__dirname, 'src/content/injected.ts'),
      },
      output: {
        entryFileNames: (chunkInfo) => {
          if (chunkInfo.name === 'background') return 'background/index.js';
          if (chunkInfo.name === 'content') return 'content/index.js';
          if (chunkInfo.name === 'injected') return 'content/injected.js';
          return 'assets/[name]-[hash].js';
        },
        chunkFileNames: 'assets/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash].[ext]',
      },
    },
  },
});
