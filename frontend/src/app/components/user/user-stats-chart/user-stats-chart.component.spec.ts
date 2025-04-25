import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserStatsChartComponent } from './user-stats-chart.component';

describe('UserStatsChartComponent', () => {
  let component: UserStatsChartComponent;
  let fixture: ComponentFixture<UserStatsChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserStatsChartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserStatsChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
