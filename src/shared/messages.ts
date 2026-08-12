export type MessageType =
  | 'SUBMISSION_DETECTED'
  | 'COMMIT_SUCCESS'
  | 'COMMIT_ERROR'
  | 'RECOVERY_START'
  | 'RECOVERY_STOP'
  | 'RECOVERY_PROGRESS'
  | 'RECOVERY_COMPLETE'
  | 'AUTH_SUCCESS'
  | 'AUTH_ERROR'
  | 'TEST_CONNECTION'
  | 'START_OAUTH'
  | 'FETCH_REPOS'
  | 'GET_STATE';

export interface Message<T = unknown> {
  type: MessageType;
  payload?: T;
}
