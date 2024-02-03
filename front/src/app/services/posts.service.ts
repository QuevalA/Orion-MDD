import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {AuthSessionService} from "./auth-session.service";
import {catchError, map, Observable, switchMap, throwError} from "rxjs";
import {TopicsService} from "./topics.service";
import {CommentsService} from "./comments.service";
import {AuthSession} from "../interfaces/auth-session";

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  private backendUrl = '/api/posts';

  constructor(
    private http: HttpClient,
    private authSessionService: AuthSessionService,
    private topicsService: TopicsService,
    private commentsService: CommentsService
  ) {
  }

  getPostsBySubscriptions(): Observable<any[]> {
    // Get subscribed topics
    return this.topicsService.getSubscribedTopicsByUser().pipe(
      switchMap(subscribedTopics => {
        // Extract topic ids from subscribed topics
        const topicIds = subscribedTopics.map(topic => topic.id);

        // Check if token exists
        const token = this.authSessionService.getToken();
        if (!token) {
          return throwError('Token is missing');
        }

        const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
        if (!authSession) {
          return throwError('Invalid token');
        }

        // Construct headers
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        let params = new HttpParams();

        // Add topicIds as query parameters
        for (const topicId of topicIds) {
          params = params.append('topicIds', topicId.toString());
        }

        return this.http.get<any[]>(this.backendUrl, {headers, params}).pipe(
          catchError(error => throwError('An error occurred'))
        );
      })
    );
  }

  getPostById(postId: string): Observable<any> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<any>(`${this.backendUrl}/${postId}`, {headers}).pipe(
      switchMap(post => {
        // Fetch comments for the post
        return this.commentsService.getCommentsByPost(postId).pipe(
          // Combine the post and comments into a single object
          map(comments => ({...post, comments})),
          catchError(error => throwError('An error occurred while fetching comments'))
        );
      }),
      catchError(error => throwError('An error occurred while fetching post'))
    );
  }

  createPost(newPostData: any): Observable<any> {
    const token = this.authSessionService.getToken();
    if (!token) {
      return throwError('Token is missing');
    }

    const authSession: AuthSession | void = this.authSessionService.isValidToken(token);
    if (!authSession) {
      return throwError('Invalid token');
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>(this.backendUrl, newPostData, {headers}).pipe(
      catchError(error => throwError('An error occurred while creating post'))
    );
  }
}
