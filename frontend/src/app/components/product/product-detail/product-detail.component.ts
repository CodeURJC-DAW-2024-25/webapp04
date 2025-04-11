import { Component } from '@angular/core';
import { ProductDTO } from '../../../dtos/product.dto';
import { ProductService } from '../../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent {
  id: number;
  product?: ProductDTO = undefined;
  user?: UserBasicDTO = undefined;
  isOwner: boolean = false;
  isAdmin: boolean = false;
  isFavorite: boolean = false;
  favorites: ProductDTO[] = [];

  constructor(private productService: ProductService, private userService: UserService, route: ActivatedRoute, private router: Router) {
    this.id = route.snapshot.params['id'];
  }

  ngOnInit() {
    this.productService.getProductDetail(this.id).subscribe({
      next: (product: ProductDTO) => {
        this.product = product;
  
        this.userService.getUser().subscribe({
          next: (user: UserBasicDTO) => {
            this.user = user;
            
            if (this.product && this.user) {
              this.userService.getAllFavorites(this.user.id).subscribe({
                next: (favorites: ProductDTO[]) => {
                  this.favorites = favorites;
                  this.isFavorite = this.favorites.some(fav => fav.id === this.product?.id);
                  console.log('isFavorite:', this.isFavorite);
                },
                error: (error) => {
                  console.error('Error fetching favorites:', error);
                }
              });
  
              this.isOwner = this.product.owner?.id === this.user.id;
              this.isAdmin = this.user.roles.includes('ADMIN');
            }
          },
          error: (error) => {
            console.error('Error fetching user:', error);
          }
        });
      },
      error: (error) => {
        console.error('Error fetching product details:', error);
      }
    });
  }
  

  addToFavorites() {
    if (!this.user) {
      this.router.navigate(['login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }
  
    if (this.product) {
      this.userService.addFavorite(this.user.id, this.product.id).subscribe({
        next: () => {
          this.isFavorite = true;
          console.log(`Producto ${this.product?.id} añadido a favoritos.`);
        },
        error: (err) => {
          console.error('Error al añadir a favoritos:', err);
        }
      });
    }
  }
  
  removeFromFavorites(): void {
    if (this.user && this.product) {
      this.userService.deleteFavorite(this.user.id, this.product.id).subscribe({
        next: () => {
          this.isFavorite = false;
          console.log(`Producto ${this.product?.id} eliminado de favoritos.`);
        },
        error: (err) => {
          console.error('Error al eliminar de favoritos:', err);
        }
      });
    }
  }

}
