import {Injectable} from '@angular/core';
import {AuthSession} from "../interfaces/auth-session";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthSessionService {

  constructor(private router: Router) {
  }

  getToken(): string | null {
    const token = localStorage.getItem('token');

    if (!token) {
      console.log('[CUSTOM LOG] no token found by getToken(), redirecting to Login page...');
      this.router.navigate(['/login']);
    }

    console.log('[CUSTOM LOG] token returned by getToken(): ', token);
    return token;
  }

  removeToken(): void {
    localStorage.removeItem('token');
  }

  isValidToken(token: string): AuthSession | void {
    try {
      const [, payloadBase64] = token.split('.');
      const payloadJson = atob(payloadBase64);
      const payloadObj = JSON.parse(payloadJson);

      if (!payloadObj || typeof payloadObj !== 'object') {
        throw new Error('Invalid payload data');
      }

      const authSession: AuthSession = {
        token: token,
        userId: payloadObj.userId,
        userEmail: payloadObj.sub,
        username: payloadObj.username,
        tokenIssuedAt: payloadObj.iat,
        tokenExpirationAt: payloadObj.exp
      };

      console.log('[CUSTOM LOG] AuthSession object returned by isValidToken(): ', authSession);
      return authSession;

    } catch (error) {
      console.error('[CUSTOM LOG] Error from isValidToken(): ', error);
      this.router.navigate(['/login']);
    }
  }

  getAuthenticatedUser(): AuthSession | null {
    const token = this.getToken();
    if (token) {
      return this.isValidToken(token) as AuthSession;
    }
    return null;
  }
}
