import { Component, Input } from '@angular/core';
import { ReviewReportService } from '../../../services/review.report.service';

@Component({
  selector: 'app-review-form',
  templateUrl: './review-form.component.html',
  styleUrl: './review-form.component.css'
})
export class ReviewFormComponent {

  @Input()
  reviewedUserId?: number; // reviewd user id

  rating: string = '';
  description: string = '';
  isVisible: boolean = false;
  infoMissing: boolean = false;

  constructor(private reviewService: ReviewReportService) { }

  toggleVisibility() {
    this.isVisible = !this.isVisible;
  }

  sendReview() {
    if (this.description === '' || this.rating === '') {
      this.infoMissing = true;
      return;
    }
    this.infoMissing = false;
    console.log('Sending review:', this.rating, this.description, this.reviewedUserId);
    this.reviewService.postReview(this.rating, this.description, this.reviewedUserId).subscribe({
      next: (review) => {
        this.isVisible = false;
        this.rating = '';
        this.description = '';
        this.reviewService.notifyReviewAdded();
      },
      error: (error) => {
        console.error('Error sending review:', error);
      }
    });

  }

}
