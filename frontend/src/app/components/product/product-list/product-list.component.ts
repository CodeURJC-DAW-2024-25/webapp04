
import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { ProductDTO } from '../../../dtos/product.dto';
import { ProductBasicDTO } from '../../../dtos/product.basic.dto';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnChanges {

  @Input() title: string = '';
  @Input() selectedCategory: string = '';
  @Input() productsFromParent: ProductDTO[] = [];

  products: (ProductDTO)[] = [];
  isLast: boolean = false;
  currentPage: number = 0;
  isLoading: boolean = false;
  fromParent: boolean = false;

  constructor(private route: ActivatedRoute, private productService: ProductService) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const name = params['name'];
      if (name) {
        this.title = `Resultados para "${name}"`;
        this.loadProductsByName(name);
      } else if (this.selectedCategory) {
        this.loadProductsByCategory();
      } else if(this.productsFromParent.length > 0){
        this.fromParent = true;
        this.loadMoreFromParent();
      }
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['selectedCategory'] && !changes['selectedCategory'].firstChange) {
      this.currentPage = 0;
      this.products = [];
      this.loadProductsByCategory();
    }
  }

  loadProductsByCategory() {
    this.isLoading = true;
    this.productService.getProductsByCategory(this.selectedCategory, this.currentPage)
      .subscribe((data: { content: ProductDTO[], last: boolean }) => {
        this.products = data.content;
        this.isLast = data.last;
        this.isLoading = false;
      });
  }

  loadProductsByName(name: string) {
      this.isLoading = true;
      this.productService.getProductsByName(name, this.currentPage).subscribe((data: { content: ProductDTO[], last: boolean }) => {
      this.products = data.content;
      this.isLast = data.last;
      this.isLoading = false;
    });
  }
    
  loadMore() {
    if (!this.isLast) {
      this.isLoading = true;
      if(!this.fromParent){
        this.productService.getProductsByCategory(this.selectedCategory, ++this.currentPage)
        .subscribe((data: { content: ProductDTO[], last: boolean }) => {
          this.products = [...this.products, ...data.content];
          this.isLast = data.last;
          this.isLoading = false;
        });
      }
    }
  }  
  
  loadMoreFromParent() {
    console.log("Cargando más productos desde el padre");
    console.log(this.productsFromParent);
    this.isLast = (this.currentPage + 1) * 8 >= this.productsFromParent.length;
    const startIdx = this.currentPage * 8;
    const endIdx = Math.min(startIdx + 8, this.productsFromParent.length);

    for (let i = startIdx; i < endIdx; i++) {
      this.productService.getProductDetail(this.productsFromParent[i].id).subscribe((detailedProduct: ProductDTO) => {
        this.products.push(detailedProduct);
      });
    }
    this.currentPage++;  
    this.isLoading = false;
  }
}


// loadFavorites() {
//   this.isLoading = true;
//   this.userService.getFavorites(this.userId, this.currentPage).subscribe({
//     next: (response) => {
//       const newProducts = response.content.filter(product =>
//         !this.products.some(existingProduct => existingProduct.id === product.id)
//       );
//       this.products = [...this.products, ...newProducts];
//       this.isLast = response.last;
//       this.isLoading = false;
//     },
//     error: (error) => {
//       console.error(error);
//       this.isLoading = false;
//     }
//   });
// }

// loadMore() {
//   if (this.isLast) return;

//   this.isLoading = true;
//   this.currentPage++;

//   if (this.isFavorites) {
//     this.loadFavorites();
//   } else {
//     this.loadProductsByCategory();
//   }

