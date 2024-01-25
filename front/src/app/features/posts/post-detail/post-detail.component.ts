import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit {

// Placeholder data
  postTitle: string = 'Sample Post Title';
  postDate: Date = new Date('2022-01-01');
  postAuthor: string = 'John Doe';
  postTopic: string = 'Angular Material';
  postContent: string = 'This is a sample post content. Lorem ipsum dolor sit amet, consectetur adipiscing elit.';

  // Placeholder comments data
  comments: { username: string; text: string }[] = [
    {username: 'User1', text: 'Great post!'},
    {username: 'User2', text: 'Interesting insights.'},
  ];

  newCommentText: string = '';

  constructor(private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    // Use ActivatedRoute to retrieve post ID from route parameters
    // this.route.snapshot.params['id'];
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }

  createComment(): void {
    if (this.newCommentText.trim() !== '') {
      this.comments.push({username: 'NewUser', text: this.newCommentText});

      // Clear input field
      this.newCommentText = '';
    }
  }
}
