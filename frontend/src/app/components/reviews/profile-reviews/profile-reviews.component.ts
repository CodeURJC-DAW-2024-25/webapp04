import { Component, Input, OnInit } from '@angular/core';
import { ReviewDTO } from '../../../dtos/review.dto';
import { ReviewReportService } from '../../../services/review.report.service';
import { UserService } from '../../../services/user.service';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';

@Component({
  selector: 'app-profile-reviews',
  templateUrl: './profile-reviews.component.html',
  styleUrls: ['./profile-reviews.component.css']
})
export class ProfileReviewsComponent implements OnInit {

  @Input() 
  userId?: number;

  reviews: ReviewDTO[] = [];
  stars: number[] = [1, 2, 3, 4, 5];
  isAdmin: boolean = false;
  currentUser: UserBasicDTO | undefined;
  loading: boolean = false;

  constructor(private reviewService: ReviewReportService, private userService: UserService) {  }

  ngOnInit() {
      this.userService.getUser().subscribe({
          next: (currentUser: UserBasicDTO | null) => {
              if (currentUser) {
                  this.currentUser = currentUser;
                  this.isAdmin = this.userService.checkAdmin(currentUser.roles);
              } else {
                  console.log('Usuario no autenticado, mostrando reseñas públicas');
              }
              this.loadReviews();
          },
          error: (err) => {
              console.error('Error fetching user:', err);
              this.loadReviews();
          }
      });
  }



  loadReviews(): void {
    this.loading = true;
    this.reviewService.getReviews(this.userId).subscribe({
      next: (data: ReviewDTO[]) => {
        this.reviews = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching reviews:', error);
        this.loading = false;
      }
    });
  }

  deleteReview(reviewId: number): void {
    this.loading = true;
    
    console.log('Antes de eliminar:', this.reviews);
  
    this.reviewService.deleteReview(reviewId).subscribe({
      next: (response: string) => {
        console.log(response);
        this.loadReviews();
        console.log('Después de eliminar, lista recargada.');
      },
      error: (error) => {
        console.error('Error deleting review:', error);
        this.loading = false;
      }
    });
  }
  
  
}
