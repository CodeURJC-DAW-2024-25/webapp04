import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css']
})
export class NewProductComponent {
  productForm: FormGroup;
  categories = ['Móviles', 'Ordenadores', 'Auriculares', 'SmartWatches', 'Otros'];
  selectedFiles: File[] = [];
  imageError: string = '';

  constructor(
    private fb: FormBuilder, 
    private router: Router, 
    private http: HttpClient, 
    private userService: UserService
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [null, [Validators.required, Validators.min(0)]],
      category: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.userService.getUser().subscribe({
      next: (user) => {
        console.log('User is logged in', user);
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  // Handle file selection
  onFileSelected(event: any): void {
    const files = Array.from(event.target.files);
    if (files.length > 5) {
      this.imageError = 'No puedes subir más de 5 imágenes.';
      return;
    }
    this.imageError = '';
    this.selectedFiles = files as File[];
  }

  // Submit the product form
  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    if (this.selectedFiles.length === 0) {
      this.imageError = 'Debes subir al menos una imagen del producto.';
      return;
    } else if (this.selectedFiles.length > 5) {
      this.imageError = 'No puedes subir más de 5 imágenes.';
      return;
    } else {
      this.imageError = '';
    }

    const productData = this.productForm.value;

    this.createProduct(productData);
  }

  // Create the product
  createProduct(productData: any): void {
    this.http.post<any>('/api/v1/products', productData).subscribe({
      next: (response) => {
        const productId = response.id;
        this.uploadImages(productId);
      },
      error: (err) => {
        console.error('Error al crear el producto', err);
      }
    });
  }

  // Upload images
  uploadImages(productId: number): void {
    let imageUploadCount = 0;
    const uploadImage = (file: File) => {
      const formData = new FormData();
      formData.append('image', file, file.name);
      return this.http.post(`/api/v1/products/${productId}/images`, formData);
    };

    // Upload images one by one
    const uploadNext = (i: number) => {
      if (i >= this.selectedFiles.length) {
        this.router.navigate([`product/${productId}`]);
        return;
      }
      uploadImage(this.selectedFiles[i]).subscribe({
        next: () => uploadNext(i + 1),
        error: (err) => console.error('Error al subir la imagen', err)
      });
    };

    uploadNext(0); // Start uploading from the first image
  }

  cancel(): void {
    this.router.navigate(['/']);
  }
}
