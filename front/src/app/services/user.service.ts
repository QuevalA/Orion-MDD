import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthSessionService} from "./auth-session.service";
import {catchError, Observable, throwError} from "rxjs";
import {AuthSession} from "../interfaces/auth-session";
import {TopicsService} from "./topics.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private backendUrl = '/api/users';

  constructor(
    private http: HttpClient,
    private authSessionService: AuthSessionService,
    private topicsService: TopicsService
  ) {
  }

  getLoggedInUser(): Observable<any> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    // Construct the URL with user ID from the AuthSession
    const url = `${this.backendUrl}/${authSession.userId}`;

    // Set headers with Authorization token
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    // Fetch user data from the API
    return this.http.get<any>(url, {headers}).pipe(
      catchError(error => throwError('An error occurred'))
    );
  }

  getSubscribedTopics(): Observable<any[]> {
    return this.topicsService.getSubscribedTopicsByUser();
  }

  subscribeToTopic(topicId: number): Observable<string> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const url = `${this.backendUrl}/${authSession.userId}/subscribe/${topicId}`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post(url, null, {headers, responseType: 'text'}).pipe(
      catchError(error => {
        console.error('(Service) Subscription failed: ', error);
        return throwError('(Service) Subscription failed');
      })
    );
  }

  unsubscribeFromTopic(topicId: number): Observable<string> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const url = `${this.backendUrl}/${authSession.userId}/unsubscribe/${topicId}`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post(url, null, {headers, responseType: 'text'}).pipe(
      catchError(error => {
        console.error('(Service) Unsubscription failed: ', error);
        return throwError('(Service) Unsubscription failed');
      })
    );
  }

  updateUser(username: string, email: string, password: string) {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const url = `${this.backendUrl}`;
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json');

    const body = {
      "username": username,
      "email": email,
      "password": password
    };

    return this.http.put(url, body, {headers, responseType: 'text'}).pipe(
      catchError(error => {
        console.error('(Service) User update failed: ', error);
        return throwError('(Service) User update failed');
      })
    );
  }
}
