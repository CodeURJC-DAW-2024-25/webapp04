import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})

export class HomePageComponent {
  selectedCategory: string = '';
  constructor(private route: ActivatedRoute) {}

  onCategorySelected(category: string) {
    this.selectedCategory = category;
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.selectedCategory = params['category'] || '';
    });
  }
}
