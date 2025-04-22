import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MapService } from '../../../services/map.service'; // Import MapService

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  currentUser!: UserBasicDTO;
  userEmail: string | null = sessionStorage.getItem('userEmail');
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
        this.mapService.getIframe(currentUser.id).subscribe({
          next: (iframeUrl: string) => {
            this.form.patchValue({
              name: currentUser.name,
              iframe: iframeUrl
            });
            this.mapService.initializeMapFromIframe(iframeUrl, this.form);
          },
          error: err => {
            console.error('Error al obtener el iframe', err);
          }
        });
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  // Submit form to update user details
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
      error: err => console.error('Error editando el usuario', err)
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
        this.cdr.detectChanges();
        this.userService.getUser().subscribe();
      },
      error: err => {
        console.error('Error al editar la imagen del perfil', err);
      }
    });
  }

}
