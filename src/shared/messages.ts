export type MessageType =
  | 'SUBMISSION_DETECTED'
  | 'COMMIT_SUCCESS'
  | 'COMMIT_ERROR'
  | 'AUTH_SUCCESS'
  | 'AUTH_ERROR'
  | 'TEST_CONNECTION'
  | 'START_OAUTH'
  | 'SYNC_GITHUB'
  | 'FETCH_REPOS'
  | 'GET_STATE'
  // Proxies a LeetCode GraphQL request through the content script,
  // which shares the user's browser cookie session (MV3 SW cannot).
  | 'PROXY_GRAPHQL';

export interface Message<T = unknown> {
  type: MessageType;
  payload?: T;
}
