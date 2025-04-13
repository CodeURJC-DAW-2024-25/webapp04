import { Component, Input } from '@angular/core';
import { ReviewDTO } from '../../../dtos/review.dto';
import { ReviewReportService } from '../../../services/review.report.service';

@Component({
  selector: 'app-review-list',
  templateUrl: './review-list.component.html',
  styleUrl: './review-list.component.css'
})
export class ReviewListComponent {

  @Input() 
  userId?: number;

  reviews: ReviewDTO[] = [];
  stars: number[] = [1, 2, 3, 4, 5];

  constructor(private reviewService: ReviewReportService) { }

  ngOnInit() {
    this.reviewService.getReviews(this.userId).subscribe({
      next: (reviews: ReviewDTO[]) => {
        console.log('Reviews:', reviews);
        this.reviews = reviews;
      },
      error: (error) => {
        console.error('Error fetching reviews:', error);
      }
    });
  }

}
