import { Component, Input } from '@angular/core';
import { UserDTO } from '../../../dtos/user.dto';
import { UserService } from '../../../services/user.service';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent {
  currentUser!: UserBasicDTO;
  editUser!: UserDTO;
  userEmail: string | null = sessionStorage.getItem('userEmail'); // User email from session storage
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ){
    this.form = this.fb.group({
      name: [this.editUser?.name || '', Validators.required],
      newPass: [''],
      repeatPass: [''],
      iframe: ['']
    });
  }

  ngOnInit(){
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

  onSubmit() {
    if (this.form.invalid) return;
  
    const { name, newPass, repeatPass, iframe } = this.form.value;
  
    if (newPass !== repeatPass) {
      alert("Las contraseñas no coinciden");
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
      error: err => console.error('Error al editar usuario', err)
    });
  }
  
  
}




