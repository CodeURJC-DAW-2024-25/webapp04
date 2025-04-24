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

  @Output() categorySelected = new EventEmitter<string>();

  selectCategory(category: string): void {
    this.selectedCategory = category;
    this.categorySelected.emit(category);
  }
}
