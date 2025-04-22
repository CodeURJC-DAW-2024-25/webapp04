import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserDTO } from '../../../dtos/user.dto';
import { UserService } from '../../../services/user.service';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MapService } from '../../../services/map.service'; // Import the MapService

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  currentUser!: UserBasicDTO;
  editUser!: UserDTO;
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
      name: [this.editUser?.name || '', Validators.required],
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
        this.userService.getUserById(currentUser.id).subscribe({
          next: (user: UserDTO) => {
            this.editUser = user;
            this.form.patchValue({
              name: user.name,
              iframe: user.iframe
            });

            // Load the iframe and initialize the map
            this.loadIframeAndInitializeMap(user.iframe);
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
        this.editUser.image = this.userService.getProfileImageUrl(this.currentUser.id);

        this.editUser.hasImage = true;
        this.cdr.detectChanges();
        this.userService.getUser().subscribe();
      },
      error: err => {
        console.error('Error uploading profile image', err);
      }
    });
  }
}
