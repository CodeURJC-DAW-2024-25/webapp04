import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MapService } from '../../../services/map.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  currentUser!: UserBasicDTO;
  isAdmin: boolean = false; // Is the user an admin
  userEmail: string | null = sessionStorage.getItem('userEmail'); // User email from session storage
  form: FormGroup;
  selectedFile!: File;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private mapService: MapService // Inject MapService
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      newPass: [''],
      repeatPass: [''],
      iframe: ['']
    });
  }

  ngOnInit() {
    if (!this.userEmail) {
      this.router.navigate(['/login']);
      return;
    }

    this.userService.getUser().subscribe({
      next: (currentUser: UserBasicDTO) => {
        this.currentUser = currentUser;
        this.isAdmin = this.userService.checkAdmin(currentUser.roles);
        if (this.isAdmin) {
          this.form.patchValue({
            name: currentUser.name,
          });
        } else {
          // Fetch iframe separately from API
          this.mapService.getUserIframe(currentUser.id).subscribe({
            next: (iframe: string) => {
              this.form.patchValue({
                name: currentUser.name,
                iframe: iframe
              });

              // Load the iframe and initialize the map
              this.loadIframeAndInitializeMap(iframe);
            },
            error: () => {
              this.form.patchValue({
                name: currentUser.name
              });

              // Initialize map with no iframe
              this.loadIframeAndInitializeMap(null);
            }
          });
        }
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  // Private method to handle iframe URL and initialize the map
  private loadIframeAndInitializeMap(iframeUrl: string | null): void {
    if (iframeUrl) {
      const matches = iframeUrl.match(/bbox=([-\d.]+),([-\d.]+),([-\d.]+),([-\d.]+)/);
      if (matches) {
        const minLng = parseFloat(matches[1]);
        const minLat = parseFloat(matches[2]);
        const maxLng = parseFloat(matches[3]);
        const maxLat = parseFloat(matches[4]);

        const centerLat = (minLat + maxLat) / 2;
        const centerLng = (minLng + maxLng) / 2;

        this.mapService.initializeMapFromIframe(iframeUrl, this.updateIframe.bind(this)); // Use MapService
      } else {
        this.mapService.initializeMapFromIframe(null, this.updateIframe.bind(this)); // Use MapService
      }
    } else {
      this.mapService.initializeMapFromIframe(null, this.updateIframe.bind(this)); // Use MapService
    }
  }

  private updateIframe(lat: string, lng: string): void {
    const iframe = this.mapService.updateIframe(lat, lng);
    this.form.patchValue({
      iframe: iframe
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    const { name, newPass, repeatPass, iframe } = this.form.value;

    if (newPass !== repeatPass) {
      alert("Passwords do not match");
      return;
    }

    const editUserDTO = {
      name,
      address: '',
      newPass,
      repeatPass,
      iframe
    };

    this.userService.updateUser(editUserDTO, this.currentUser.id).subscribe({
      next: () => this.router.navigate(['/profile']),
      error: err => console.error('Error updating user', err)
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.uploadProfileImage();
    }
  }

  private uploadProfileImage(): void {
    const formData = new FormData();
    formData.append('image', this.selectedFile, this.selectedFile.name);

    this.userService.updateProfileImage(formData, this.currentUser.id).subscribe({
      next: () => {
        const timestamp = new Date().getTime();
        this.currentUser.image = this.userService.getProfileImageUrl(this.currentUser.id) + `?t=${timestamp}`;
        this.currentUser.hasImage = true;

        this.cdr.detectChanges();
        this.userService.getUser().subscribe(); // Refresh user info
      },
      error: err => {
        console.error('Error uploading profile image', err);
      }
    });
  }
}
