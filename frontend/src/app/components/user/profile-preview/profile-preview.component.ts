import { Component, Input } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { UserDTO } from '../../../dtos/user.dto';
import { Subscription } from 'rxjs';
import { ReviewReportService } from '../../../services/review.report.service';
import { ReviewDTO } from '../../../dtos/review.dto';
import { MapService } from '../../../services/map.service';

@Component({
  selector: 'app-profile-preview',
  templateUrl: './profile-preview.component.html',
  styleUrl: './profile-preview.component.css'
})
export class ProfilePreviewComponent {

  @Input() 
  userId?: number;

  private reviewAddedSubscription!: Subscription;

  user?: UserDTO;
  rating: number = 0;

  constructor(private userService: UserService, private reviewService: ReviewReportService, private mapService: MapService) { 
    this.reviewAddedSubscription = this.reviewService.reviewAdded$.subscribe(() => {
      this.ngOnInit();
    });
  }

  ngOnInit() {
    if (this.userId) {
      this.userService.getUserById(this.userId).subscribe({
        next: (user) => {
          this.user = user;
          this.mapService.visualizeMapInContainer(user.id, 'preview-map');
          console.log('User:', user);
          this.rating = 0;
          for (let i=0; i < user.reviews.length; i++) {
            this.rating += user.reviews[i].rating;
          }
          if (user.reviews.length > 0) {
            console.log('Rating:', this.rating, 'Reviews:', user.reviews.length);
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
