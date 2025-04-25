import { Component, Input, OnInit } from '@angular/core';
import { StatsService } from '../../../services/stats.service';
import { Color, ScaleType } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-user-stats-chart',
  templateUrl: './user-stats-chart.component.html',
  styleUrls: ['./user-stats-chart.component.css']
})
export class UserStatsChartComponent implements OnInit {
  @Input() userId: number = 0; 
  chartData: { name: string; series: { name: string; value: number }[] }[] = [];
  view: [number, number] = [700, 400];

  // Chart options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true; 

  colorScheme: Color = {
    name: 'customScheme',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#d35400', '#3498db'] // Purchase = orange, Sale = blue
  };

  // Custom legend labels
  legendLabels: Record<string, string> = {
    purchase: 'Compras',
    sale: 'Ventas'
  };

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.fetchStats();
  }

  fetchStats(): void {
    this.statsService.getStats(this.userId).subscribe(data => {
      this.chartData = this.statsService.processStats(data).map(item => ({
        name: this.legendLabels[item.name] || item.name, // Use custom legend labels
        series: item.series
      }));
    });
  }

  // Function to format Y-axis labels to show only integers
  yAxisTickFormatting(value: number): string {
    if (Number.isInteger(value)) {
      return value.toString(); 
    }
    return ''; 
  }
}
