# leetie OAuth Proxy — Vercel Deployment Guide

This lightweight serverless function proxies OAuth code exchange requests securely between the `leetie` browser extension and GitHub's OAuth server.

## 1-Click Vercel Deployment

1. Go to [vercel.com/new](https://vercel.com/new) and import your `leetie` repository (`Shreeprasandh/leetie`).
2. Set the **Root Directory** to `vercel`.
3. Add the following **Environment Variables**:
   - `GITHUB_CLIENT_ID` = `Ov23li296L9RxwhuLXOv`
   - `GITHUB_CLIENT_SECRET` = `d966ac1f77d1fac250e5f7c96abdd2dfb48d6186`
4. Click **Deploy**.
5. Copy your deployed Vercel URL (e.g. `https://leetie-proxy.vercel.app/api/exchange`) and paste it into `leetie` Extension Settings.
