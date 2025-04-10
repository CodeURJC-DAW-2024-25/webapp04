import { Component } from '@angular/core';
import { ProductDTO } from '../../../dtos/product.dto';
import { ProductService } from '../../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';

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

  constructor(private productService: ProductService, private userService: UserService, route: ActivatedRoute) {
    this.id = route.snapshot.params['id'];
  }

  ngOnInit() {

    this.productService.getProductDetail(this.id).subscribe({
      next: (product: ProductDTO) => {
        this.product = product;

        this.userService.getUser().subscribe({
          next: (user: UserBasicDTO) => {
            this.user = user;
            
            if(this.product !== undefined && this.user !== undefined) {
              this.userService.isFavorite(this.user.id, this.product.id).subscribe({
                next: (isFavorite: boolean) => {
                  this.isFavorite = isFavorite;
                  console.log('isFavorite:', this.isFavorite);
                }
              });
    
              if(this.product !== undefined && this.user !== undefined) {
                this.isOwner = this.product.owner.id === this.user.id;
              }
          
              if(this.user !== undefined) {
                this.isAdmin = this.user.roles.includes('ADMIN');
              }
    
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


}
