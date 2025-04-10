import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { UserDTO } from '../../../dtos/user.dto';
import { UserService } from '../../../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-profile-preview',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'] // Asegúrate de que el nombre del archivo de estilos sea correcto
})
export class ProfileComponent {
  currentUser: UserBasicDTO | undefined;  // Current user
  user?: UserDTO;                         // Profile preview user
  isOwnProfile: boolean = false;          // Is the profile being previewed the owner

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute, private sanitizer: DomSanitizer) {}


  ngOnInit(){
    this.userService.getUser().subscribe((currentUser: UserBasicDTO) => {
      this.currentUser = currentUser;  
      this.userService.getUserById(currentUser.id).subscribe((user: UserDTO) => {
        this.user = user;
      });
    });
  }

  getSafeIframe(iframe: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(iframe);
  }

  
}
