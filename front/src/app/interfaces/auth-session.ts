export interface AuthSession {
  token: string;
  userId: number;
  userEmail: string;
  username: string;
  tokenIssuedAt: number;
  tokenExpirationAt: number;
}
