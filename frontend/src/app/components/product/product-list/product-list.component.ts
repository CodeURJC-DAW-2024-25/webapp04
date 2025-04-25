
import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { ProductDTO } from '../../../dtos/product.dto';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnChanges {

  @Input() title: string = '';
  @Input() selectedCategory: string = '';
  @Input() productsFromParent: ProductDTO[] = [];
  @Input() fromProfile: boolean = false;

  products: (ProductDTO)[] = [];
  isLast: boolean = false;
  currentPage: number = 0;
  isLoading: boolean = false;
  fromParent: boolean = false;
  private isCategoryLoaded: boolean = false;

  constructor(private route: ActivatedRoute, private productService: ProductService) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const name = params['name'];
      if (name) {
        this.title = `Resultados para "${name}"`;
        this.loadProductsByName(name);
      } else if (this.selectedCategory) {
        if (!this.isCategoryLoaded) {
          this.isCategoryLoaded = true;
          this.loadProductsByCategory();
        }
      } else if (this.productsFromParent.length > 0) {
        this.fromParent = true;
        this.loadMoreFromParent();
      }
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['selectedCategory'] && !changes['selectedCategory'].firstChange) {
      this.currentPage = 0;
      this.products = [];
      this.isCategoryLoaded = false;
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
    this.isLoading = true;
    // Gives Angular time to update the view and show the spinner before the fast responses hide it again
    setTimeout(() => {
      this.loadParentProductsBatch();
    }, 300);
  }

  private loadParentProductsBatch() {
    this.isLast = (this.currentPage + 1) * 8 >= this.productsFromParent.length;
    const startIdx = this.currentPage * 8;
    const endIdx = Math.min(startIdx + 8, this.productsFromParent.length);
    let pendingRequests = endIdx - startIdx;

    for (let i = startIdx; i < endIdx; i++) {
      this.productService.getProductDetail(this.productsFromParent[i].id).subscribe((detailedProduct: ProductDTO) => {
        this.products.push(detailedProduct);
        pendingRequests--;
        if (pendingRequests === 0) {
          this.isLoading = false;
          this.currentPage++;
        }
      });
    }
  }
}

