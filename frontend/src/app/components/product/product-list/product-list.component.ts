import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductDTO } from '../../../dtos/product.dto';
import { ProductService } from '../../../services/product.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnChanges {

  @Input() title: string = '';
  @Input() selectedCategory: string = '';
  @Input() isFavorites: boolean = false;

  products: ProductDTO[] = [];
  isLast: boolean = false;
  currentPage: number = 0;
  isLoading: boolean = false;
  userId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const name = params['name'];
      const filter = params['filter'];
  
      this.products = [];
      this.currentPage = 0;
  
      if (filter === 'favorites') {
        this.isFavorites = true;
        this.userService.getUser().subscribe({
          next: user => {
            this.userId = user.id;
            this.loadFavorites();
          },
          error: err => {
            console.error('No se pudo obtener el usuario:', err);
            this.isLoading = false;
          }
        });
      } else {
        this.isFavorites = false;
        if (name) {
          this.title = `Resultados para "${name}"`;
          this.loadProductsByName(name);
        } else if (this.selectedCategory) {
          this.loadProductsByCategory();
        }
      }
    });
  }
  

  ngOnChanges(changes: SimpleChanges) {
    if (!this.isFavorites && changes['selectedCategory'] && !changes['selectedCategory'].firstChange) {
      this.currentPage = 0;
      this.products = [];
      this.loadProductsByCategory();
    }
  }

  loadProductsByCategory() {
    this.isLoading = true;
    this.productService.getProductsByCategory(this.selectedCategory, this.currentPage)
      .subscribe((data) => {
        this.products = [...this.products, ...data.content];
        this.isLast = data.last;
        this.isLoading = false;
      });
  }

  loadProductsByName(name: string) {
    this.isLoading = true;
    this.productService.getProductsByName(name, this.currentPage)
      .subscribe((data) => {
        this.products = [...this.products, ...data.content];
        this.isLast = data.last;
        this.isLoading = false;
      });
  }

  loadFavorites() {
    this.isLoading = true;
    this.userService.getFavorites(this.userId, this.currentPage).subscribe({
      next: (response) => {
        const newProducts = response.content.filter(product =>
          !this.products.some(existingProduct => existingProduct.id === product.id)
        );
        this.products = [...this.products, ...newProducts];
        this.isLast = response.last;
        this.isLoading = false;
      },
      error: (error) => {
        console.error(error);
        this.isLoading = false;
      }
    });
  }

  loadMore() {
    if (this.isLast) return;

    this.isLoading = true;
    this.currentPage++;

    if (this.isFavorites) {
      this.loadFavorites();
    } else {
      this.loadProductsByCategory();
    }
  }

}
