import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-signin',
  templateUrl: './login-signin.component.html',
  styleUrl: './login-signin.component.css'
})
export class LoginSigninComponent {

  login: boolean = true;
  name: string = '';
  email: string = '';
  password: string = '';
  password2: string = '';

  validName: number = 0;
  validEmail: number = 0;
  validPassword: number = 0;
  loginError: boolean = false;
  signinError: boolean = false;

  constructor(private userService: UserService, private router: Router) { }

  toggleLogin() {
    this.login = !this.login;

    this.name = '';
    this.email = '';
    this.password = '';
    this.password2 = '';

    this.validName = 0;
    this.validEmail = 0;
    this.validPassword = 0;
    this.loginError = false;
    this.signinError = false;
  }

  submitForm() {
      if (this.login) {
          this.userService.loginUser(this.email, this.password).subscribe({
              next: (response) => {
                if (response.status === 'SUCCESS') {
                  window.location.href = '/';  // Redirecting with refresh after login
                } else {
                  this.loginError = true;
                }
              },
              error: (error) => {
                this.loginError = true;
                console.error('Login error:', error);
              }
          });
      } else {
          if (this.validateForm()) {
              this.userService.registerUser(this.name, this.email, this.password, this.password2).subscribe({
                  next: (response) => {
                      if (response) {
                        this.signinError = false;
                        this.login = true;
                      } else {
                        this.signinError = true;
                      }
                  },
                  error: (error) => {
                    this.signinError = true;
                    console.error('Registration error:', error);
                  }
              });
          }
      }
  }

  private validateForm(): boolean {

    const isNameValid = this.validateName(this.name);
    const isEmailValid = this.validateMail(this.email);
    const isPasswordValid = this.validatePassword(this.password, this.password2);

    return isNameValid && isEmailValid && isPasswordValid;
  }

  // Validate username
  private validateName(name: string): boolean {
    if (!name) {  // Check if the name is empty
      this.validName = 1;
      return false;
    } else if (name.length > 16) {  // Check if the name is longer than 16 characters
      this.validName = 2;
      return false;
    } else {
      this.validName = 0;
      return true;
    }
  }

  // Validate mail
  private validateMail(mail: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;  // Email regular expression
    if (!mail) {  // Check if the email is empty
      this.validEmail = 1;
      return false;
    } else if (!emailRegex.test(mail)) {  // Check if the email is valid
      this.validEmail = 2;
      return false;
    } else {
      this.validEmail = 0;
      return true;
    }
  }

  // Validate passwords
  private validatePassword(password: string, confirmPassword: string): boolean {
    if (!password || !confirmPassword) {  // Check if the password is empty
      this.validPassword = 1;
      return false;
    } else if (password !== confirmPassword) {  // Check if the passwords match
      this.validPassword = 2;
      return false;
    } else {
      this.validPassword = 0;
      return true;
    }
  }

}
