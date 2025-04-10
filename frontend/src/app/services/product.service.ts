import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO } from '../dtos/product.dto';


@Injectable({
    providedIn: 'root'
})
export class ProductService {
    constructor(private http: HttpClient) { }

    getTopProducts(): Observable<{content: ProductDTO[]}> {
        let url = '/api/v1/products?recommended=true';
        return this.http.get<{content: ProductDTO[]}>(url);
    }
    
    getOtherProducts(pageNumber?: number): Observable<{content: ProductDTO[], last: boolean}> {
        if (pageNumber!== undefined) {
            let url = `/api/v1/products?available=true&page=${pageNumber}`;
            return this.http.get<{content: ProductDTO[], last: boolean}>(url);
        }
        let url = '/api/v1/products?available=true';
        return this.http.get<{content: ProductDTO[], last: boolean}>(url);
    }
    
    getProductDetail(productId: number): Observable<ProductDTO> {
        let url = `/api/v1/products/${productId}`;
        return this.http.get<ProductDTO>(url);
    }

    getProductsByCategory(category: string, pageNumber: number = 0): Observable<{ content: ProductDTO[], last: boolean }> {
        const url = `/api/v1/products?available=true&category=${encodeURIComponent(category)}&page=${pageNumber}`;
        return this.http.get<{ content: ProductDTO[], last: boolean }>(url);
    }
      
}