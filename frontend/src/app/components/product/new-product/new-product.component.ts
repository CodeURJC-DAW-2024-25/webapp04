import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css']
})
export class NewProductComponent {

  productId?: number;
  images: string[] = []; // used while editing a product
  @ViewChild('imageInput') imageInput!: ElementRef<HTMLInputElement>;

  productForm: FormGroup;
  categories = ['Móviles', 'Ordenadores', 'Auriculares', 'SmartWatches', 'Otros'];
  selectedFiles: File[] = [];
  imageError: string = '';

  constructor(
    private fb: FormBuilder, 
    private router: Router,
    private userService: UserService,
    private productService: ProductService,
    private route: ActivatedRoute
  ) {

    this.productId = this.route.snapshot.params['id'] ? +this.route.snapshot.params['id'] : undefined;

    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [null, [Validators.required, Validators.min(0)]],
      category: ['', Validators.required],
    });

    if(this.productId!==undefined){
      
      this.productService.getProductDetail(this.productId).subscribe({
        next: (product) => {
          this.userService.getUser().subscribe({
            next: (user) => {
              if (product.owner.id !== user.id) {
                this.router.navigate(['/']);
              } else {
                this.productForm.patchValue({
                  name: product.name,
                  description: product.description,
                  price: product.price,
                  category: product.category
                });
                this.images = product.imageUrls;
              }
            }
          });
        },
        error: (err) => {
            console.error('Error fetching product details', err);
          this.router.navigate(['/']);
        }
      });
    }
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

    if(this.productId === undefined){
      if (this.selectedFiles.length === 0) {
        this.imageError = 'Debes subir al menos una imagen del producto.';
        return;
      } else if (this.selectedFiles.length > 5) {
        this.imageError = 'No puedes subir más de 5 imágenes.';
        return;
      } else {
        this.imageError = '';
      }
  }

    const productData = this.productForm.value;

    if(this.productId === undefined) {
      this.createProduct(productData);
    } else {
      this.updateProduct(this.productId, productData);
    }
  }

  // Create the product
  createProduct(productData: any): void {
    this.productService.createProduct(productData).subscribe({
      next: (response) => {
        const productId = response.id;
        this.uploadImages(productId);
      },
      error: (err) => {
        console.error('Error creating the product', err);
      }
    });
  }

  // Upload images
  uploadImages(productId: number): void {
    let imageUploadCount = 0;
    const uploadImage = (file: File) => {
      const formData = new FormData();
      formData.append('image', file, file.name);
      return this.productService.addImage(productId, formData);
    };

    // Upload images one by one
    const uploadNext = (i: number) => {
      if (i >= this.selectedFiles.length) {
        this.router.navigate([`product/${productId}`]);
        return;
      }
      uploadImage(this.selectedFiles[i]).subscribe({
        next: () => uploadNext(i + 1),
        error: (err) => console.error('Error uploading image', err)
      });
    };

    uploadNext(0); // Start uploading from the first image
  }

  // Update the product
  updateProduct(productId: number, productData: any): void {
    this.productService.updateProduct(productData, productId).subscribe({
      next: (response) => {
        this.router.navigate([`product/${productId}`]);
      },
      error: (err) => {
        console.error('Error updating the product', err);
      }
    });
  }

  addImageEditing(): void{
    if (this.images.length < 5) {
      if(this.productId !== undefined && this.selectedFiles.length === 1) {
        const formData = new FormData();
        formData.append('image', this.selectedFiles[0]);
        this.productService.addImage(this.productId, formData).subscribe(
          {
            next: (response) => {
              this.selectedFiles = []; // Clear the selected files after upload
              this.productService.getProductDetail(this.productId!).subscribe({
                next: (product) => {
                  this.images = product.imageUrls; // Update the images after upload
                  this.imageInput.nativeElement.value = ''; // Clear the input field
                }
              });
            },
            error: (err) => {
              console.error('Error uploading image', err);
            }
          });
      }
    } else {
      console.error('No more than 5 images');
    }
  }

  deleteImage(imageId: string): void {
    let id: number = +imageId;
    if(this.productId !== undefined){
      this.productService.deleteImage(this.productId, id).subscribe({
        next: () => {
          this.productService.getProductDetail(this.productId!).subscribe({
            next: (product) => {
              this.images = product.imageUrls; // Update the images after deletion
            }
          });
        },
        error: (err) => {
          console.error('Error deleting image', err);
        }
      });
    }

  }

  cancel(): void {
    this.router.navigate(['/']);
  }
}
