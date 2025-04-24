import { Component } from '@angular/core'; 
import { DomSanitizer } from '@angular/platform-browser';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { UserDTO } from '../../../dtos/user.dto';
import { UserService } from '../../../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
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
  filterLoaded: boolean = false;          // Is the favorites/reviews/purchases/sales list filterLoaded
  isAdmin: boolean = false;               // Is the logged user an admin


  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private mapService: MapService 
  ) {
    this.profileId = route.snapshot.paramMap.get('id') ? parseInt(route.snapshot.paramMap.get('id')!) : undefined;
  }

  // Verify if the user is logged in or anonymous
  ngOnInit() {
    this.userService.getUser().subscribe({
      next: (currentUser: UserBasicDTO) => {
        this.currentUser = currentUser;
        this.isAdmin = this.userService.checkAdmin(currentUser.roles);
        if (this.profileId === undefined) {
          this.loadOwnProfile();
          this.profileId = currentUser.id;
        } else {
          this.loadOtherProfile(this.profileId);
        }
        // Check if the user is an admin and is viewing their own profile
        if (this.isAdmin && this.profileId === currentUser.id) {
          this.router.navigate(['/adminProfile']);
        }
      },
      error: () => {
        if (this.profileId !== undefined) {
          this.loadOtherProfile(this.profileId); //Load other profile
        } else {
          this.router.navigate(['/login']);
        }
      }
    });
  }

  // Load own profile
  private loadOwnProfile() {
    this.isOwnProfile = true;
    this.userService.getUser().subscribe({
      next: (currentUser: UserBasicDTO) => {
        this.currentUser = currentUser;
        this.userService.getUserById(currentUser.id).subscribe({
          next: (user: UserDTO) => {
            this.user = user;
            if (!this.isAdmin) {
              this.setFilterFromQueryParams();
              this.loadUserMap(user.id);
            }
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

  // Load other user profile
  private loadOtherProfile(profileId: number) {
    this.isOwnProfile = false;
    this.userService.getUserById(profileId).subscribe({
      next: (user: UserDTO) => {
        this.user = user; 
        if (!this.isAdmin) {
          this.loadUserMap(user.id);
        }
        this.loadUserMap(user.id);
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

  // Check if the profile is an admin profile
  isAdminProfile() {
    return this.userService.checkAdmin(this.user?.roles || []);
  }
  
  // Detects if the filter has changed in the URL and updates the filter variable accordingly
  setFilterFromQueryParams() {
    this.route.queryParams.subscribe(params => {
      const filterFromUrl = params['filter'];
      if (filterFromUrl && filterFromUrl !== this.filter) {
        this.filter = filterFromUrl;
        this.onCategorySelected(this.filter);
      }
    });
  }

  // Logout function
  logout(): void {
    this.userService.logout().subscribe({
      next: () => {
        sessionStorage.removeItem('userEmail'); // Delete the email from session storage
        this.router.navigateByUrl('/').then(() => {
          window.location.reload();
        });
      },
      error: (err) => {
        console.error('Error al cerrar sesión', err);
      }
    });
  }

  // Function to get the iframe from the backend and sanitize it
  private loadUserMap(userId: number): void {
    this.mapService.visualizeMapFromUserIframe(userId);
  }

  deleteOtherAccount() {
    if (this.isAdmin && this.user && this.profileId) {
      if (!this.user.roles.includes('ADMIN')) {
        this.userService.deleteUser(this.profileId, this.isOwnProfile).subscribe({
          next: () => {
            // Realiza alguna acción después de la eliminación (por ejemplo, redirigir a la página principal)
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error("Error al eliminar la cuenta", err);
          }
        });
      } else {
        alert("No puedes eliminar la cuenta de un administrador.");
      }
    } else {
      alert("Solo los administradores pueden eliminar cuentas de otros usuarios.");
    }
  }
  
  // Loads the correct list of favorites/historySalee/historyPurchase depending on the selected filter
  onCategorySelected(filter: string) {
    this.filter = filter;
    console.log("Filter seleccionado:", this.filter);
    if (this.user == undefined) {
      console.log("No se ha cargado el usuario");
      return;
    }
    if (filter === 'favorites') {
      this.filterLoaded = false;
      console.log(this.user);
      console.log(this.user.id);
      this.userService.getAllFavorites(this.user.id).subscribe({
        next: (response) => {
          this.favorites = response;
          this.filterLoaded = true;
          console.log("Favoritos cargados:", this.favorites);
        },
        error: (err) => {
          console.error("Error al obtener favoritos", err);
          this.filterLoaded = true;
        }
      });
    }
    else if (filter === 'historyPurchase') {

    }
  }


  
}
