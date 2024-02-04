import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PostsService} from "../../../services/posts.service";
import {CommentsService} from "../../../services/comments.service";
import {AuthSessionService} from "../../../services/auth-session.service";

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit {

  post: any = {};
  comments: { commentAuthorUsername: string; content: string }[] = [];
  newCommentText: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postsService: PostsService,
    private commentsService: CommentsService,
    private authSessionService: AuthSessionService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const postId = params['id'];
      this.fetchPostDetails(postId);
    });
  }

  fetchPostDetails(postId: string): void {
    this.postsService.getPostById(postId).subscribe(
      (postDetails: any) => {
        this.post = postDetails;
        this.comments = postDetails.comments;
      },
      error => {
        console.error('Error fetching post details:', error);
      }
    );
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }

  createComment(): void {
    if (this.newCommentText.trim() !== '') {
      const postId = this.post.id; // Get the ID of the current post
      const content = this.newCommentText; // Get the content of the new comment

      // Call the createComment method from CommentsService
      this.commentsService.createComment(postId, content).subscribe(
        (newComment: any) => {
          // Get authenticated user data from AuthSessionService
          const authSession = this.authSessionService.getAuthenticatedUser();

          // Ensure authSession is not null before accessing properties
          if (authSession) {
            // Update the comments array with the newly created comment and author information
            this.comments.push({
              commentAuthorUsername: authSession.username,
              content: newComment.content
            });
          } else {
            console.error('Error: Authenticated user data is null.');
          }

          // Clear input field
          this.newCommentText = '';
        },
        error => {
          console.error('Error creating comment:', error);
        }
      );
    }
  }
}
