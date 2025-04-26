import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

export class NavbarComponent {
  @Input() selectedCategory: string = '';
  @Input() profileScreen: boolean = false;
  @Input() isOwner: boolean = false;
  @Input() userId: number | undefined;
  @Input() isAdmin: boolean = false;
  @Output() categorySelected = new EventEmitter<string>();

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const url = this.router.url.split('?')[0];
  
      if (url.startsWith('/profile')) {
        const queryParams = this.route.snapshot.queryParams;
        const filterParam = queryParams['filter'];
  
        if (filterParam) {
          this.selectedCategory = filterParam;
        } else {
          this.selectedCategory = '';
        }
      } else {
        const queryParams = this.route.snapshot.queryParams;
        const categoryParam = queryParams['category'];
  
        if (categoryParam) {
          this.selectedCategory = categoryParam;
        } else {
          this.selectedCategory = '';
        }
      }
    });
  }

  selectCategory(category: string): void {
    this.selectedCategory = category;
    this.categorySelected.emit(category);
  }
  
  get showCategories(): boolean {
    return !this.profileScreen;
  }
  
  get showFavorites(): boolean {
    return this.profileScreen && this.isOwner && !this.isAdmin;
  }
  
  get showHistory(): boolean {
    return this.profileScreen && this.isOwner && !this.isAdmin;
  }
  
  get showSales(): boolean {
    return this.profileScreen && this.isOwner && !this.isAdmin;
  }
  
  get showUserReviews(): boolean {
    return this.profileScreen && !this.isAdmin && !this.isOwner;
  }
  
}
