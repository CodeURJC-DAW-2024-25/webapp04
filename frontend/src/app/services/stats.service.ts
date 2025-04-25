import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface StatData {
  month: number;
  type: string;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class StatsService {
  constructor(private http: HttpClient) {}

  getStats(userId: number): Observable<StatData[]> {
    return this.http.get<StatData[]>(`/api/v1/users/${userId}/stats`);
  }

  // Analyze the data and return it in a format suitable for the chart
  processStats(data: StatData[]): { name: string; series: { name: string; value: number }[] }[] {
    const allMonths = Array.from({ length: 12 }, (_, i) => i + 1);
    const types = ['purchase', 'sale'];

    const grouped: Record<string, { [month: number]: number }> = {
      purchase: {},
      sale: {}
    };

    data.forEach(d => {
      grouped[d.type][d.month] = d.quantity;
    });

    return types.map(type => ({
      name: type,
      series: allMonths.map(month => ({
        name: this.getMonthName(month),
        value: grouped[type][month] ?? 0
      }))
    }));
  }

  // This function returns the month name in short format (e.g., Jan, Feb, Mar)
  private getMonthName(month: number): string {
    const date = new Date();
    date.setMonth(month - 1);
    const monthName = date.toLocaleString('default', { month: 'short' });
    return monthName.charAt(0).toUpperCase() + monthName.slice(1);
  }
}
