import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {TopicsService} from "../../../services/topics.service";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-topics-list',
  templateUrl: './topics-list.component.html',
  styleUrls: ['./topics-list.component.scss']
})
export class TopicsListComponent implements OnInit {

  topics: any[] = [];
  subscribedTopics: any[] = [];
  isSmallScreen: boolean | undefined;

  constructor(
    private breakpointObserver: BreakpointObserver,
    private topicsService: TopicsService,
    private userService: UserService
  ) {
    this.breakpointObserver
      .observe([Breakpoints.Small, Breakpoints.XSmall])
      .subscribe((result) => {
        this.isSmallScreen = result.matches;
      });
  }

  ngOnInit(): void {
    this.fetchTopicsAndSubscribedTopics();
  }

  subscribeToTopic(topicId: number): void {
    this.userService.subscribeToTopic(topicId).subscribe(
      () => {
        // Update button text immediately after successful subscription
        const topicIndex = this.topics.findIndex(topic => topic.id === topicId);
        if (topicIndex !== -1) {
          this.topics[topicIndex].subscribed = true;
        }
      },
      (error) => {
        console.error('(Component) Subscription failed:', error);
      }
    );
  }

  private fetchTopicsAndSubscribedTopics() {
    this.topicsService.getAllTopics().subscribe(
      (topicsData) => {
        this.topics = topicsData;
        // Fetch subscribed topics after getting all topics
        this.topicsService.getSubscribedTopicsByUser().subscribe(
          (subscribedTopicsData) => {
            this.subscribedTopics = subscribedTopicsData;
            // Mark subscribed topics in the topics list
            this.topics.forEach(topic => {
              if (this.subscribedTopics.find(subscribedTopic => subscribedTopic.id === topic.id)) {
                topic.subscribed = true;
              }
            });
          },
          (subscribedTopicsError) => {
            console.error('(Component) Error fetching subscribed topics:', subscribedTopicsError);
          }
        );
      },
      (topicsError) => {
        console.error('(Component) Error fetching topics:', topicsError);
      }
    );
  }
}
