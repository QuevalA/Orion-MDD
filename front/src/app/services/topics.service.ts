import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {AuthSessionService} from "./auth-session.service";
import {AuthSession} from "../interfaces/auth-session";
import {Topic} from "../interfaces/topic";

@Injectable({
  providedIn: 'root'
})
export class TopicsService {

  private backendUrl = '/api/topics';

  constructor(
    private http: HttpClient,
    private authSessionService: AuthSessionService
  ) {
  }

  getAllTopics(): Observable<Topic[]> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<Topic[]>(this.backendUrl, {headers}).pipe(
      catchError(error => throwError('An error occurred'))
    );
  }

  getSubscribedTopicsByUser(): Observable<any[]> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `${this.backendUrl}/subscribed/${authSession.userId}`;

    return this.http.get<any[]>(url, {headers}).pipe(
      catchError(error => throwError('An error occurred'))
    );
  }
}
