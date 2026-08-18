import { defineConfig, Plugin, build as viteBuild } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve, dirname } from 'path';
import { copyFileSync, mkdirSync, existsSync, readFileSync, writeFileSync, rmSync } from 'fs';

function copyManifestPlugin(): Plugin {
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

// Extension background service worker and content scripts MUST be self-contained
// single-file bundles without relative imports into shared /assets/ chunks.
function standaloneScriptsPlugin(): Plugin {
  return {
    name: 'standalone-scripts',
    async closeBundle() {
      const scripts = [
        { entryName: 'background', file: 'background/index.js', path: resolve(__dirname, 'src/background/index.ts') },
        { entryName: 'content', file: 'content/index.js', path: resolve(__dirname, 'src/content/index.ts') },
        { entryName: 'injected', file: 'content/injected.js', path: resolve(__dirname, 'src/content/injected.ts') },
      ];

      for (const script of scripts) {
        await viteBuild({
          configFile: false,
          plugins: [react()],
          resolve: { alias: { '@': resolve(__dirname, 'src') } },
          build: {
            outDir: 'dist',
            emptyOutDir: false,
            rollupOptions: {
              input: { [script.entryName]: script.path },
              output: {
                entryFileNames: script.file,
                inlineDynamicImports: true,
              },
            },
          },
        });
      }
    },
  };
}

export default defineConfig({
  base: '', // CRITICAL: forces relative asset paths in built HTML.
  // Chrome extensions cannot resolve absolute /assets/ paths.
  plugins: [react(), flattenHtmlPlugin(), standaloneScriptsPlugin(), copyManifestPlugin()],
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
      },
      output: {
        entryFileNames: 'assets/[name]-[hash].js',
        chunkFileNames: 'assets/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash].[ext]',
      },
    },
  },
});
