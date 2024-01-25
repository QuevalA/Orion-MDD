import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginIdentifier: string = '';
  password: string = '';

  constructor() {
  }

  login() {
    // Determine whether the input is an email or a username
    const isEmail = this.isEmail(this.loginIdentifier);

    if (isEmail) {
      // Login with email
    } else {
      // Login with username
    }
  }

  ngOnInit(): void {
  }

  private isEmail(input: string): boolean {
    // Basic email validation logic
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(input);
  }
}
