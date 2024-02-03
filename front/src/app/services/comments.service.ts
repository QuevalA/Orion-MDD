import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthSessionService} from "./auth-session.service";
import {catchError, Observable, throwError} from "rxjs";
import {AuthSession} from "../interfaces/auth-session";

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private backendUrl = '/api/comments';

  constructor(
    private http: HttpClient,
    private authSessionService: AuthSessionService
  ) {
  }

  getCommentsByPost(postId: string): Observable<any[]> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<any[]>(`${this.backendUrl}/${postId}`, {headers}).pipe(
      catchError(error => throwError('An error occurred'))
    );
  }

  createComment(postId: string, content: string) {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const body = {content, postId};

    return this.http.post<any>(this.backendUrl, body, {headers}).pipe(
      catchError(error => throwError('An error occurred'))
    );
  }
}
