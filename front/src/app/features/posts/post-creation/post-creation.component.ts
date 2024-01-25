import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-post-creation',
  templateUrl: './post-creation.component.html',
  styleUrls: ['./post-creation.component.scss']
})
export class PostCreationComponent implements OnInit {

  // Placeholder
  topics: string[] = ['Technology', 'Science', 'Programming', 'Miscellaneous'];
  selectedTopic: string = '';
  newPostTitle: string = '';
  newPostContent: string = '';

  constructor(private router: Router) {
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }

  submitForm(): void {
    if (this.selectedTopic && this.newPostTitle && this.newPostContent) {
      // Placeholder to display the form values in the console
      console.log('Selected Topic:', this.selectedTopic);
      console.log('New Post Title:', this.newPostTitle);
      console.log('New Post Content:', this.newPostContent);

      // Placeholder to redirect to post detail page
      this.router.navigate(['/posts/detail']);
    }
  }

  ngOnInit(): void {
  }
}
