import { Component } from '@angular/core';
import { ProductDTO } from '../../../dtos/product.dto';
import { ProductService } from '../../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';
import { Router } from '@angular/router';
import { ChatService } from '../../../services/chat.service';

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
  hasBoughtProduct: boolean = false; // has bought this exact product
  hasBoughtUser: boolean = false; // has bought any product from this user

  constructor(private productService: ProductService, private userService: UserService, private chatService: ChatService, route: ActivatedRoute, private router: Router) {
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
              this.userService.isFavorite(this.user.id, this.product.id).subscribe({
                next: (isFavorite: boolean) => {
                  this.isFavorite = isFavorite;
                  console.log('isFavorite:', this.isFavorite);
                }
              });

              this.isOwner = this.product.owner.id === this.user.id;

              this.isAdmin = this.user.roles.includes('ADMIN');

              this.userService.checkHasBoughtUser(this.user.id, this.product.owner.id).subscribe({
                next: (hasBoughtUser: boolean) => {
                  this.hasBoughtUser = hasBoughtUser;
                }
              });

              this.userService.checkHasBoughtProduct(this.user.id, this.product.id).subscribe({
                next: (hasBoughtProduct: boolean) => {
                  this.hasBoughtProduct = hasBoughtProduct;
                }
              });
            }
          },
          error: (error) => {
            console.error('Error fetching user:', error);
          }
        });
      },
      error: (error) => {
        console.error('Error fetching product details:', error);
        this.router.navigate(['/']);
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
            console.log(`Product ${this.product?.id} added to favorites.`);
        },
        error: (err) => {
          console.error('Error adding to favorites:', err);
        }
      });
    }
  }
  
  removeFromFavorites(): void {
    if (this.user && this.product) {
      this.userService.deleteFavorite(this.user.id, this.product.id).subscribe({
        next: () => {
          this.isFavorite = false;
            console.log(`Product ${this.product?.id} removed from favorites.`);
        },
        error: (err) => {
          console.error('Error deleting from favorites:', err);
        }
      });
    }
  }

  deleteProduct(): void {
    this.productService.deleteProduct(this.id).subscribe({
      next: () => {
        console.log('Product deleted successfully');
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Error deleting the product:', err);
      }
    });
  }

  contactSeller(): void {
    if (!this.user) {
      this.router.navigate(['login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }
  
    if (!this.product?.id) return;
    this.chatService.createChat(this.product.id).subscribe({
      next: (chat) => {
        this.router.navigate(['/chats', chat.id]);
      },
      error: (err) => {
        console.error('Error al crear el chat', err);
      }
    });
  }
  

}
