import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginIdentifier: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {
  }

  login() {
    const loginRequest = {email: this.loginIdentifier, password: this.password};

    this.authService.login(loginRequest).subscribe(
      (response) => {
        // Store token in localStorage
        localStorage.setItem('token', response.token);

        this.router.navigate(['/posts']);
      },
      (error) => {
        console.error('Login failed:', error);
      }
    );
  }

  ngOnInit(): void {
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
