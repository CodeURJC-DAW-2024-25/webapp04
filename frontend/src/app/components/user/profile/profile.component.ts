import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { UserDTO } from '../../../dtos/user.dto';
import { UserService } from '../../../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { SafeHtml } from '@angular/platform-browser';
import { ProductDTO } from '../../../dtos/product.dto';
import { ReviewDTO } from '../../../dtos/review.dto';
import { ReviewReportService } from '../../../services/review.report.service';

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
  reviews: ReviewDTO[] = [];

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute, private sanitizer: DomSanitizer,private reviewService: ReviewReportService) {
    this.profileId = route.snapshot.paramMap.get('id') ? parseInt(route.snapshot.paramMap.get('id')!) : undefined;
  }

  ngOnInit() {
    if (this.profileId === undefined) {
      this.loadOwnProfile();
    } else {
      this.loadOtherProfile(this.profileId);
    }
  }
  //Loads the profile of the current user
  private loadOwnProfile() {
    this.isOwnProfile = true;
    this.userService.getUser().subscribe({
      next: (currentUser: UserBasicDTO) => {
        this.currentUser = currentUser;
        this.userService.getUserById(currentUser.id).subscribe({
          next: (user: UserDTO) => {
            this.user = user;
            this.setFilterFromQueryParams();
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

  //Loads the profile of another user
  private loadOtherProfile(profileId:number) {
    this.isOwnProfile = false;
      this.userService.getUserById(profileId).subscribe({
        next: (user: UserDTO) => {
          this.user = user;
          this.userService.getUser().subscribe({
            next: (currentUser: UserBasicDTO) => {
              this.currentUser = currentUser;
              this.setFilterFromQueryParams();
              if (this.user && this.user.id === this.currentUser?.id) {
                this.isOwnProfile = true;
              }
            }
          });          
        },
        error: () => {
          //cambiar a página de error de que no se encontró el usuario
          console.log("Error: No se encuentra el perfil");
          this.router.navigate(['']);
        }
      });
    }


  //Detects if the filter has changed in the URL and updates the filter variable accordingly
  setFilterFromQueryParams() {
    this.route.queryParams.subscribe(params => {
      const filterFromUrl = params['filter'];
      if (filterFromUrl && filterFromUrl !== this.filter) {
        this.filter = filterFromUrl;
        this.onCategorySelected(this.filter);
      }
    });
  }

  //Logout function
  logout(): void {
    this.userService.logout().subscribe({
      next: () => {
        sessionStorage.removeItem('userEmail'); //Delete the email from session storage
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
  getSafeIframe(iframe: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(iframe);
  }

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
