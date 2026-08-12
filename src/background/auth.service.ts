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

  // Exchange code for access token via proxy
  // Abort after 10s so a dead proxy doesn't hang the OAuth flow indefinitely
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 10_000);

  let tokenRes: Response;
  try {
    tokenRes = await fetch(proxyUrl, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ code }),
      signal: controller.signal,
    });
  } catch (err: any) {
    if (err.name === 'AbortError') {
      throw new Error('Token exchange timed out. Check your proxy URL in Settings.');
    }
    throw err;
  } finally {
    clearTimeout(timeoutId);
  }

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
