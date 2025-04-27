import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { ReportDTO } from '../dtos/report.dto';
import { ReviewDTO } from '../dtos/review.dto';
import { tap } from 'rxjs/operators';


@Injectable({
    providedIn: 'root'
})
export class ReviewReportService {

    private reportCreatedSource = new Subject<void>();
    reportCreated$ = this.reportCreatedSource.asObservable();

    constructor(private http: HttpClient) { }


    getReviews(userId: number | undefined): Observable<ReviewDTO[]> {
        if (!userId) {
            console.error('User ID is undefined');
            return undefined as any;
        }
        let url = `/api/v1/users/${userId}/reviews`;
        return this.http.get<ReviewDTO[]>(url);
    }
    
    postReport(reason: string, description: string, productId: number | undefined): Observable<ReportDTO> {
        if (!productId || !description) {
            console.error('Information missing');
            return undefined as any;
        }
        let url = '/api/v1/reports';
        return this.http.post<ReportDTO>(url, { reason, description, productId }).pipe(
            tap(() => this.reportCreatedSource.next())
          );

    }

    postReview(rating: string, description: string, userId: number | undefined): Observable<ReviewDTO> {
        if (!userId || !description) {
            console.error('Information missing');
            return undefined as any;
        }
        let url = `/api/v1/users/${userId}/reviews`;
        return this.http.post<ReviewDTO>(url, { rating, description, userId });
    }

    deleteReview(reviewId: number): Observable<string> {
        const url = `/api/v1/reviews/${reviewId}`;
        return this.http.delete(url, { responseType: 'text' });
    }

    //For refreshing the reviews list when a new review is added
    private reviewAddedSource = new Subject<void>();

    reviewAdded$ = this.reviewAddedSource.asObservable();
    
    notifyReviewAdded(): void {
        this.reviewAddedSource.next();
    }

    getReports(): Observable<ReportDTO[]> {
        let url = '/api/v1/reports'; 
        return this.http.get<ReportDTO[]>(url);
    }

    deleteReport(reportId: number): Observable<string> {
        let url = `/api/v1/reports/${reportId}`;
        return this.http.delete<string>(url);
    }
}