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

  products: ProductDTO[] = [];
  isLast: boolean = false;
  currentPage: number = 0;
  isLoading: boolean = false;

  constructor(private route: ActivatedRoute, private productService: ProductService) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const name = params['name'];
      if (name) {
        this.title = `Resultados para "${name}"`;
        this.loadProductsByName(name);
      } else if (this.selectedCategory) {
        this.loadProductsByCategory();
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
      this.productService.getProductsByCategory(this.selectedCategory, ++this.currentPage)
        .subscribe((data: { content: ProductDTO[], last: boolean }) => {
          this.products = [...this.products, ...data.content];
          this.isLast = data.last;
          this.isLoading = false;
        });
    }
  }

}
