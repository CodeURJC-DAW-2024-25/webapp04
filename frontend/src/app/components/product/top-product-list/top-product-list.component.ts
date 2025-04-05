import { Component } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { ProductDTO } from '../../../dtos/product.dto';

@Component({
  selector: 'app-top-product-list',
  templateUrl: './top-product-list.component.html',
  styleUrl: './top-product-list.component.css'
})
export class TopProductListComponent {

  products: ProductDTO[] = [];

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productService.getTopProducts().subscribe((data: {content: ProductDTO[]}) => {
      this.products = data.content;
    });
  }

}
