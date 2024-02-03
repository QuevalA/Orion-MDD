import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {UserService} from "../../../services/user.service";
import {Topic} from "../../../interfaces/topic";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {

  user: any = {};
  isSmallScreen: boolean | undefined;
  loading: boolean = false;
  error: string | undefined;

  constructor(
    private breakpointObserver: BreakpointObserver,
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) {
    this.breakpointObserver.observe([Breakpoints.Small, Breakpoints.XSmall]).subscribe(result => {
      this.isSmallScreen = result.matches;
    });
  }

  ngOnInit(): void {
    this.fetchUserData();
  }

  fetchUserData(): void {
    this.loading = true;
    this.userService.getLoggedInUser().subscribe(
      (user) => {
        this.user = user;
        // Fetch subscribed topics once user data is fetched
        this.fetchSubscribedTopics();
        this.loading = false;
      },
      (error) => {
        console.error('Error fetching user data:', error);
        this.error = 'Failed to load user data.';
        this.loading = false;
      }
    );
  }

  // Fetch subscribed topics for the current user
  fetchSubscribedTopics(): void {
    this.userService.getSubscribedTopics().subscribe(
      (subscribedTopics) => {
        // Update user object with subscribed topics
        this.user.subscribedTopics = subscribedTopics;
      },
      (error) => {
        console.error('Error fetching subscribed topics:', error);
      }
    );
  }

  hasSubscribedTopics(): boolean {
    return this.user && this.user.subscribedTopics && this.user.subscribedTopics.length > 0;
  }

  unsubscribeFromTopic(topicId: number): void {
    this.userService.unsubscribeFromTopic(topicId).subscribe(
      () => {
        if (this.user.subscribedTopics) {
          // Filter out the unsubscribed topic and create a new array
          this.user.subscribedTopics = this.user.subscribedTopics.filter((topic: Topic) => topic.id !== topicId);
        }
      },
      (error) => {
        console.error('(Component) Unsubscription failed:', error);
      }
    );
  }

  trackByTopicId(index: number, topic: Topic): number {
    return topic.id;
  }

  logout(): void {
    this.authService.logout().subscribe(
      () => {
        this.router.navigate(['/']);
      },
      (error) => {
        console.error('Logout failed:', error);
      }
    );
  }
}
