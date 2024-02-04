import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  userForm: FormGroup;

  username: string = '';
  email: string = '';
  password: string = '';
  passwordFormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(8),
    this.passwordValidator.bind(this)
  ]);

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
    this.userForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        this.passwordValidator.bind(this)
      ]]
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

  register() {
    if (this.userForm.valid) {
      const registerRequest = this.userForm.value;
      this.authService.register(registerRequest).subscribe(
        (response) => {
          this.router.navigate(['/login']);
        },
        (error) => {
          console.error('Registration failed:', error);
        }
      );
    }
  }

  ngOnInit(): void {
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
