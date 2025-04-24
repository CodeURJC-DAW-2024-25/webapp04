import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

export class NavbarComponent {
  selectedCategory: string = '';
  @Input() profileScreen: boolean = false;
  @Input() isOwner: boolean = false;
  @Input() userId: number | undefined;
  @Input() isAdmin: boolean = false;

  @Output() categorySelected = new EventEmitter<string>();

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
