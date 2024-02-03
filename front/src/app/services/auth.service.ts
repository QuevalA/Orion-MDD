import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {RegisterRequest} from "../interfaces/register-request";
import {LoginRequest} from "../interfaces/login-request";
import {catchError, Observable, tap, throwError} from "rxjs";
import {AuthSessionService} from "./auth-session.service";
import {AuthSession} from "../interfaces/auth-session";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private pathService = '/api/auth';

  constructor(private httpClient: HttpClient, private authSessionService: AuthSessionService) {
  }

  public register(registerRequest: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/public/register`, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<any> {
    return this.httpClient.post<any>(`${this.pathService}/public/login`, loginRequest);
  }

  public logout(): Observable<void | string> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.httpClient.post(`${this.pathService}/logout`, {}, {headers, responseType: 'text'}).pipe(
      tap((response: string) => {
        if (response === 'Logout successful') {
          this.authSessionService.removeToken();
        } else {
          throw new Error('Unexpected response from logout endpoint');
        }
      }),
      catchError((error) => {
        console.error('Logout failed:', error);
        return throwError('Logout failed');
      })
    );
  }
}
