import { Component } from '@angular/core';
import { UserBasicDTO } from '../../dtos/user.basic.dto';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  isAdmin: boolean = false;
  isLogged: boolean = false;
  user?: UserBasicDTO;
  searchQuery: string = '';
  private userSubscription?: Subscription;

  constructor(private userService: UserService, private router: Router) {
    this.userService.getUser().subscribe({
      next: (user: UserBasicDTO) => {
        console.log(user);
        this.user = user;
        this.isAdmin = user.roles.includes('ADMIN');
        this.isLogged = true;
      }, 
      error: error => {
        this.isAdmin = false;
        this.isLogged = false;
        this.user = undefined;
      }
    });
    this.userSubscription = this.userService.userChanges$.subscribe((user) => {
      if (user) {
        this.user = user;
        this.isAdmin = user.roles.includes('ADMIN');
        this.isLogged = true;
        this.user.image = this.userService.getProfileImageUrl(user.id);
      }
    });
  }  

  

  onSearch(event: Event): void {

    const trimmed = this.searchQuery.trim();
    
    if (trimmed) {
      this.router.navigate(['/products'], { queryParams: { name: trimmed } });
    } else {
      this.router.navigate(['']);
    }

    this.searchQuery = '';
  }
  
}
