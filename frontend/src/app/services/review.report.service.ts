import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReportDTO } from '../dtos/report.dto';
import { ReviewDTO } from '../dtos/review.dto';


@Injectable({
    providedIn: 'root'
})
export class ReviewReportService {

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
        return this.http.post<ReportDTO>(url, { reason, description, productId });

    }

    postReview(rating: string, description: string, userId: number | undefined): Observable<ReviewDTO> {
        if (!userId || !description) {
            console.error('Information missing');
            return undefined as any;
        }
        let url = `/api/v1/users/${userId}/reviews`;
        return this.http.post<ReviewDTO>(url, { rating, description, userId });
    }

}