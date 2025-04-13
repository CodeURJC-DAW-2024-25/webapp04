import { Component } from '@angular/core';
import { UserBasicDTO } from '../../dtos/user.basic.dto';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

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
    })
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
