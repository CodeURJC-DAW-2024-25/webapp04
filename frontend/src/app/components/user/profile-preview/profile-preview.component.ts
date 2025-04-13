import { Component, Input } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { UserDTO } from '../../../dtos/user.dto';

@Component({
  selector: 'app-profile-preview',
  templateUrl: './profile-preview.component.html',
  styleUrl: './profile-preview.component.css'
})
export class ProfilePreviewComponent {

  @Input() 
  userId?: number;

  user?: UserDTO;
  rating: number = 0;

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    console.log('User ID:', this.userId);
    if (this.userId) {
      this.userService.getUserById(this.userId).subscribe({
        next: (user) => {
          this.user = user;
          for (let review in user.reviews) {
            this.rating += user.reviews[review].rating;
          }
          if (user.reviews.length > 0) {
            this.rating = parseFloat((this.rating / user.reviews.length).toFixed(2));
          }
        },
        error: (error) => {
          console.error('Error fetching user data:', error);
        }
      });
    }
  }

}
