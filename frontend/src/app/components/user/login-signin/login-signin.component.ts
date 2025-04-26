import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { ValidationService } from '../../../services/validation.service';  // Import validation service

@Component({
  selector: 'app-login-signin',
  templateUrl: './login-signin.component.html',
  styleUrls: ['./login-signin.component.css']
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

  constructor(
    private userService: UserService, 
    private router: Router, 
    private validationService: ValidationService  // Inject validation service
  ) { }

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
            this.userService.getUser().subscribe({
              next: (user) => {
                sessionStorage.setItem('userEmail', this.email);
                this.router.navigate(['/'])
              },
              error: (err) => {
                sessionStorage.removeItem('userEmail');
                console.error('Error obteniendo el usuario tras login:', err);
                this.loginError = true;
              }
            });
          } else {
            sessionStorage.removeItem('userEmail');
            this.loginError = true;
          }
        },
        error: (error) => {
          sessionStorage.removeItem('userEmail');
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
    const isNameValid = this.validationService.validateName(this.name);
    const isEmailValid = this.validationService.validateMail(this.email);
    const isPasswordValid = this.validationService.validatePassword(this.password, this.password2);

    this.validName = isNameValid.errorCode;
    this.validEmail = isEmailValid.errorCode;
    this.validPassword = isPasswordValid.errorCode;

    return isNameValid.valid && isEmailValid.valid && isPasswordValid.valid;
  }
}
