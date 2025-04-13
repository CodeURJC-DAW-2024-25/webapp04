import { Component, Input } from '@angular/core';
import { ReviewReportService } from '../../../services/review.report.service';

@Component({
  selector: 'app-report-form',
  templateUrl: './report-form.component.html',
  styleUrl: './report-form.component.css'
})
export class ReportFormComponent {

  @Input()
  productId?: number;

  selectedIssue: string = '';
  description: string = '';
  isVisible: boolean = false;
  infoMissing: boolean = false;

  constructor(private reportService: ReviewReportService) { }

  toggleVisibility() {
    this.isVisible = !this.isVisible;
  }

  sendReport() {
    if (this.description === '' || this.selectedIssue === '') {
      this.infoMissing = true;
      return;
    }
    this.infoMissing = false;
    this.reportService.postReport(this.selectedIssue, this.description, this.productId).subscribe({
      next: (report) => {
        this.isVisible = false;
        this.selectedIssue = '';
        this.description = '';
      },
      error: (error) => {
        console.error('Error sending report:', error);
      }
    });
  }

}
