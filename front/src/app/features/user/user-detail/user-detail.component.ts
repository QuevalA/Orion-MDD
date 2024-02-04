import {Component, OnInit, ViewChild} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {UserService} from "../../../services/user.service";
import {Topic} from "../../../interfaces/topic";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";
import {FormControl, NgForm, Validators} from "@angular/forms";

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {

  @ViewChild('userForm') userForm!: NgForm;

  user: any = {};
  isSmallScreen: boolean | undefined;
  loading: boolean = false;
  error: string | undefined;

  username: string = '';
  email: string = '';
  password: string = '';
  passwordFormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(8),
    this.passwordValidator.bind(this)
  ]);

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

  passwordValidator(control: FormControl) {
    const value: string = control.value;
    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasDigit = /\d/.test(value);
    const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(value);

    const isValid = hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    return isValid ? null : {'invalidPassword': true};
  }

  ngOnInit(): void {
    this.fetchUserData();
  }

  fetchUserData(): void {
    this.loading = true;
    this.userService.getLoggedInUser().subscribe(
      (user) => {
        this.user = user;
        this.username = user.username; // set initial username value
        this.email = user.email; // set initial email value

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

  updateUser(): void {
    if (this.userForm.valid && this.passwordFormControl.valid) {
      this.loading = true;
      this.userService.updateUser(this.username, this.email, this.password).subscribe(
        () => {
          this.loading = false;
          window.location.reload();
        },
        (error) => {
          console.error('Error updating user:', error);
          this.loading = false;
        }
      );
    } else {
      console.error('Form is invalid or password does not meet the requirements.');
    }
  }
}
