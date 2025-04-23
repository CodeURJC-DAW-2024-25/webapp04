import { Component, Input } from '@angular/core';
import { ReviewDTO } from '../../../dtos/review.dto';
import { ReviewReportService } from '../../../services/review.report.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-review-list',
  templateUrl: './review-list.component.html',
  styleUrl: './review-list.component.css'
})
export class ReviewListComponent {

  @Input() 
  userId?: number;

  private reviewAddedSubscription!: Subscription;

  reviews: ReviewDTO[] = [];
  stars: number[] = [1, 2, 3, 4, 5];

  constructor(private reviewService: ReviewReportService) {  }

  ngOnInit() {
    console.log('User ID recibido en review-list:', this.userId);
    this.reviewAddedSubscription = this.reviewService.reviewAdded$.subscribe(() => {
      this.refreshReviews();
    });
    this.refreshReviews();
  }

  refreshReviews() {
    this.reviewService.getReviews(this.userId).subscribe({
      next: (reviews: ReviewDTO[]) => {
        console.log('Reviews:', reviews); // Verifica si el backend está enviando datos
        this.reviews = reviews;
        console.log('Reviews after assignment:', this.reviews); // Verifica si reviews se actualiza correctamente
      },
      error: (error) => {
        console.error('Error fetching reviews:', error);
      }
    });
  }
  

}
