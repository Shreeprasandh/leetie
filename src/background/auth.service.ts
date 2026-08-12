// Web Crypto API AES-GCM encryption/decryption module for GitHub Access Tokens

const ENCRYPTION_SALT = 'leetie_auth_salt_v1';

async function getKey(): Promise<CryptoKey> {
  const encoder = new TextEncoder();
  const keyMaterial = await crypto.subtle.importKey(
    'raw',
    encoder.encode(ENCRYPTION_SALT),
    { name: 'PBKDF2' },
    false,
    ['deriveKey']
  );

  return crypto.subtle.deriveKey(
    {
      name: 'PBKDF2',
      salt: encoder.encode('leetie_salt'),
      iterations: 100000,
      hash: 'SHA-256',
    },
    keyMaterial,
    { name: 'AES-GCM', length: 256 },
    false,
    ['encrypt', 'decrypt']
  );
}

export async function encryptToken(plainToken: string): Promise<string> {
  if (!plainToken) return '';
  const key = await getKey();
  const encoder = new TextEncoder();
  const iv = crypto.getRandomValues(new Uint8Array(12));
  const encrypted = await crypto.subtle.encrypt(
    { name: 'AES-GCM', iv },
    key,
    encoder.encode(plainToken)
  );

  const combined = new Uint8Array(iv.length + encrypted.byteLength);
  combined.set(iv, 0);
  combined.set(new Uint8Array(encrypted), iv.length);

  return btoa(String.fromCharCode(...combined));
}

export async function decryptToken(encryptedBase64: string): Promise<string> {
  if (!encryptedBase64) return '';
  try {
    const key = await getKey();
    const combined = Uint8Array.from(atob(encryptedBase64), (c) => c.charCodeAt(0));
    const iv = combined.slice(0, 12);
    const data = combined.slice(12);

    const decrypted = await crypto.subtle.decrypt(
      { name: 'AES-GCM', iv },
      key,
      data
    );

    return new TextDecoder().decode(decrypted);
  } catch (err) {
    console.error('[leetie] Decryption failed, returning empty string', err);
    return '';
  }
}

export async function initiateOAuthFlow(clientId: string, proxyUrl: string): Promise<string> {
  if (typeof chrome === 'undefined' || !chrome.identity?.launchWebAuthFlow) {
    throw new Error('OAuth flow requires Chrome Extension environment with chrome.identity permission.');
  }

  const redirectUri = chrome.identity.getRedirectURL();
  const authUrl = `https://github.com/login/oauth/authorize?client_id=${encodeURIComponent(clientId)}&scope=repo&redirect_uri=${encodeURIComponent(redirectUri)}`;

  const redirectUrl = await new Promise<string>((resolve, reject) => {
    chrome.identity.launchWebAuthFlow(
      {
        url: authUrl,
        interactive: true,
      },
      (responseUrl) => {
        if (chrome.runtime.lastError) {
          return reject(new Error(chrome.runtime.lastError.message));
        }
        if (!responseUrl) {
          return reject(new Error('OAuth flow was cancelled or failed.'));
        }
        resolve(responseUrl);
      }
    );
  });

  const urlObj = new URL(redirectUrl);
  const code = urlObj.searchParams.get('code');

  if (!code) {
    throw new Error('No authorization code returned from GitHub.');
  }

  // Exchange code for access token via proxy or GitHub API
  const tokenRes = await fetch(proxyUrl, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ code }),
  });

  if (!tokenRes.ok) {
    const errData = await tokenRes.json().catch(() => ({}));
    throw new Error(errData.error || `Token exchange failed (${tokenRes.status})`);
  }

  const tokenData = await tokenRes.json();
  if (!tokenData.access_token) {
    throw new Error('Access token missing from exchange response.');
  }

  return tokenData.access_token;
}
