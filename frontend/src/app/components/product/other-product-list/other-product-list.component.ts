import { Component } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { ProductDTO } from '../../../dtos/product.dto';

@Component({
  selector: 'app-other-product-list',
  templateUrl: './other-product-list.component.html',
  styleUrl: './other-product-list.component.css'
})
export class OtherProductListComponent {

  products: ProductDTO[] = [];
  isLast: boolean = false;
  currentPage: number = 0;
  isLoading: boolean = false;

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productService.getOtherProducts().subscribe((data: {content: ProductDTO[], last: boolean}) => {
      this.products = data.content;
    });
  }

  loadMore() {
    this.isLoading = true;
    this.productService.getOtherProducts(++this.currentPage).subscribe((data: {content: ProductDTO[], last: boolean}) => {
      this.products = [...this.products, ...data.content];
      this.isLast = data.last;
      this.isLoading = false;
    });
  }

}
