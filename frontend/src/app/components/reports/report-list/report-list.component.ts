import { Component, OnInit } from '@angular/core';
import { ReviewReportService } from '../../../services/review.report.service';
import { ReportDTO } from '../../../dtos/report.dto';

@Component({
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.css']
})
export class ReportListComponent implements OnInit {

  reports: ReportDTO[] = []; 
  loading: boolean = true; 

  constructor(private reportService: ReviewReportService) { }

  ngOnInit(): void {
    this.fetchReports();
  
    this.reportService.reportCreated$.subscribe(() => {
      console.log('Nuevo reporte creado, recargando...');
      this.fetchReports(); 
    });
  }

  fetchReports(): void {
    this.reportService.getReports().subscribe({
      next: (data: ReportDTO[]) => {
        this.reports = data; 
        this.loading = false; 
      },
      error: (error: ReportDTO[]) => {
        console.error('Error fetching reports:', error);
        this.loading = false;
      }
    });
  }

  deleteReport(reportId: number): void {
    this.reportService.deleteReport(reportId).subscribe({
      next: (response) => {
        this.reports = this.reports.filter(report => report.id !== reportId);
      },
      error: (error) => {
        console.error('Error deleting report:', error);
      }
    });
  }
  
}
