import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor() { }

  // Validate username
  validateName(name: string): { valid: boolean, errorCode: number } {
    if (!name) {  // Check if the name is empty
      return { valid: false, errorCode: 1 };
    } else if (name.length > 16) {  // Check if the name is longer than 16 characters
      return { valid: false, errorCode: 2 };
    } else {
      return { valid: true, errorCode: 0 };
    }
  }

  // Validate email
  validateMail(mail: string): { valid: boolean, errorCode: number } {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;  // Email regular expression
    if (!mail) {  // Check if the email is empty
      return { valid: false, errorCode: 1 };
    } else if (!emailRegex.test(mail)) {  // Check if the email is valid
      return { valid: false, errorCode: 2 };
    } else {
      return { valid: true, errorCode: 0 };
    }
  }

  // Validate passwords
  validatePassword(password: string, confirmPassword: string): { valid: boolean, errorCode: number } {
    if (!password || !confirmPassword) {  // Check if the password is empty
      return { valid: false, errorCode: 1 };
    } else if (password !== confirmPassword) {  // Check if the passwords match
      return { valid: false, errorCode: 2 };
    } else {
      return { valid: true, errorCode: 0 };
    }
  }

  // Validate password update
  updatePassword(password: string, confirmPassword: string): { valid: boolean, errorCode: number } {
    if(password !== confirmPassword) {  // Check if the passwords match
      return { valid: false, errorCode: 2 };
    }else{
      return { valid: true, errorCode: 0 };
    }
  }
}
