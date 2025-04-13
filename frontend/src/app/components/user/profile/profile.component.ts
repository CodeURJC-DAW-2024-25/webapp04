import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { UserDTO } from '../../../dtos/user.dto';
import { UserService } from '../../../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'] // Asegúrate de que el nombre del archivo de estilos sea correcto
})
export class ProfileComponent {
  currentUser: UserBasicDTO | undefined;  // Current user
  user?: UserDTO;                         // Profile preview user
  isOwnProfile: boolean = false;          // Is the profile being previewed the owner
  profileId: number | undefined;          // Profile id
  category: string = '';                  // Category selected from profile navbar

  constructor(private userService: UserService, private router: Router, route: ActivatedRoute, private sanitizer: DomSanitizer) {
    this.profileId = route.snapshot.paramMap.get('id') ? parseInt(route.snapshot.paramMap.get('id')!) : undefined;
  }

  ngOnInit() {
    if (this.profileId === undefined) {
      this.isOwnProfile = true;
      this.userService.getUser().subscribe((currentUser: UserBasicDTO) => {
        this.currentUser = currentUser;
        this.userService.getUserById(currentUser.id).subscribe((user: UserDTO) => {
          console.log("Usuario actual");
          console.log(currentUser);
          console.log("Usuario del perfil actual");
          console.log(user);
          this.user = user;
        });
      });
    } else {
      this.isOwnProfile = false;
      this.userService.getUserById(this.profileId).subscribe((user: UserDTO) => {
        console.log("Usuario del perfil");
        console.log(user);
        this.user = user;
      });
    }
  }

  getSafeIframe(iframe: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(iframe);
  }

  onCategorySelected(category: string) {
    this.category = category;
  }

}
