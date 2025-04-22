import { Component } from '@angular/core';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { UserDTO } from '../../../dtos/user.dto';
import { UserService } from '../../../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { SafeHtml } from '@angular/platform-browser';
import { ProductDTO } from '../../../dtos/product.dto';
import { ReviewDTO } from '../../../dtos/review.dto';
import { ReviewReportService } from '../../../services/review.report.service';
import { MapService } from '../../../services/map.service'; // Import MapService

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  currentUser: UserBasicDTO | undefined;  // Current user
  user?: UserDTO;                         // Profile preview user
  isOwnProfile: boolean = false;          // Is the profile being previewed the owner
  profileId: number | undefined;          // Profile id
  favorites: ProductDTO[] = [];           // User favorites
  filter: string = '';                    // Category selected from profile navbar
  loaded: boolean = false;                // Is the favorites/reviews/purchases/sales list loaded
  reviews: ReviewDTO[] = [];              // Reviews of other users
  filterLoaded: boolean = false;          // Is the favorites/reviews/purchases/sales list filterLoaded
  iframeSafe: SafeHtml | undefined;       // Safe iframe for displaying

         
  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    // private sanitizer: DomSanitizer,
    private reviewService: ReviewReportService,
    private mapService: MapService // Inject MapService
  ) {
    this.profileId = route.snapshot.paramMap.get('id') ? parseInt(route.snapshot.paramMap.get('id')!) : undefined;
  }

  ngOnInit() {
    this.userService.getUser().subscribe({
      next: (currentUser: UserBasicDTO) => {
        this.currentUser = currentUser;
        if (this.profileId === undefined) {
          this.loadOwnProfile();
        } else {
          this.loadOtherProfile(this.profileId);
        }
      },
      error: () => {
        if (this.profileId !== undefined) {
          this.loadOtherProfile(this.profileId); // Load other profile
        } else {
          this.router.navigate(['/login']);
        }
      }
    });
  }

  private loadOwnProfile() {
    this.isOwnProfile = true;
    this.userService.getUser().subscribe({
      next: (currentUser: UserBasicDTO) => {
        this.currentUser = currentUser;
        this.userService.getUserById(currentUser.id).subscribe({
          next: (user: UserDTO) => {
            this.user = user;
            this.mapService.getIframe(currentUser.id).subscribe({
              next: (iframeUrl: string) => {
                this.iframeSafe = this.mapService.getSafeIframe(iframeUrl); // Get sanitized iframe
              }
            });
          },
          error: () => {
            this.router.navigate(['/login']);
          }
        });
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  private loadOtherProfile(profileId: number) {
    this.isOwnProfile = false;
    this.userService.getUserById(profileId).subscribe({
      next: (user: UserDTO) => {
        this.user = user;
        if (this.currentUser && this.user.id === this.currentUser.id) {
          this.isOwnProfile = true;
        }
      },
      error: () => {
        console.log("Error: No se encuentra el perfil");
        this.router.navigate(['']);
      }
    });
  }

  logout(): void {
    this.userService.logout().subscribe({
      next: () => {
        sessionStorage.removeItem('userEmail');
        this.router.navigateByUrl('/').then(() => {
          window.location.reload();
        });
      },
      error: (err) => {
        console.error('Error al cerrar sesión', err);
      }
    });
  }
  
  //Function to get the iframe from the backend and sanitize it
  // getSafeIframe(iframe: string): SafeHtml {
  //   return this.sanitizer.bypassSecurityTrustHtml(iframe);
  // }

  //Loads the correct list of favorites/reviews/sales/purchases depending on the selected filter
  onCategorySelected(filter: string) {
    this.filter = filter;
    console.log("Filter seleccionado:", this.filter);
    if (filter === 'favorites' && this.user?.id !== undefined) {
      this.loaded = false; 
      console.log(this.user);
      console.log(this.user.id);
      this.userService.getAllFavorites(this.user.id).subscribe({
        next: (response) => {
          this.favorites = response;
          this.loaded = true; 
          console.log("Favoritos cargados:", this.favorites);
        },
        error: (err) => {
          console.error("Error al obtener favoritos", err);
          this.loaded = true; 
        }
      });
    }

    // else if (filter === 'reviews' && this.user?.id !== undefined) {
    //   console.log("Cargar reseñas para el usuario", this.user.id);
    //   this.loaded = false;
  
    //   this.reviewService.getReviews(this.user.id).subscribe({
    //     next: (response) => {
    //       this.reviews = response;
    //       this.loaded = true;
    //       console.log("Reseñas cargadas:", this.reviews);
    //     },
    //     error: (err) => {
    //       console.error("Error al obtener reseñas", err);
    //       this.loaded = true;
    //     }
    //   });
    // }

  }
  
  
}
