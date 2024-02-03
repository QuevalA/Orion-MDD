import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {PostsService} from "../../../services/posts.service";
import {TopicsService} from "../../../services/topics.service";
import {Topic} from "../../../interfaces/topic";

@Component({
  selector: 'app-post-creation',
  templateUrl: './post-creation.component.html',
  styleUrls: ['./post-creation.component.scss']
})
export class PostCreationComponent implements OnInit {

  topics: Topic[] = [];
  selectedTopic: string = '';
  newPostTitle: string = '';
  newPostContent: string = '';

  constructor(
    private router: Router,
    private postsService: PostsService,
    private topicsService: TopicsService
  ) {
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }

  submitForm(): void {
    if (this.selectedTopic && this.topics) {
      const selectedTopic = this.topics.find(topic => topic.name === this.selectedTopic);
      if (selectedTopic) {
        const newPostData = {
          title: this.newPostTitle,
          content: this.newPostContent,
          topicId: selectedTopic.id
        };

        this.postsService.createPost(newPostData).subscribe(
          (response) => {
            // Redirect to post detail page
            this.router.navigate(['/posts/', response.id]);
          },
          (error) => {
            console.error('Error creating post:', error);
          }
        );
      } else {
        console.error('Selected topic not found.');
      }
    } else {
      console.error('Selected topic or topics list is undefined.');
    }
  }

  ngOnInit(): void {
    this.loadTopics();
  }

  loadTopics(): void {
    this.topicsService.getAllTopics().subscribe(
      (topics: Topic[]) => {
        this.topics = topics;
      },
      (error) => {
        console.error('Error fetching topics:', error);
      }
    );
  }
}
