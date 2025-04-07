import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

export class NavbarComponent {
  selectedCategory: string = '';

  @Output() categorySelected = new EventEmitter<string>();

  selectCategory(category: string): void {
    this.selectedCategory = category;
    this.categorySelected.emit(category);
  }
}
