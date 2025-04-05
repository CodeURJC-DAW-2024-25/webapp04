import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO } from '../dtos/product.dto';


@Injectable({
    providedIn: 'root'
})
export class ProductService {
    constructor(private http: HttpClient) { }
    
    //TODO: Puede ser que haya que poner un endpoint para las imagenes en la API para que
    //todas las peticiones vayan a /api/v1 y no haya que poner la config extra que hay en el proxy
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
    
    getProductDetail(productId: string): Observable<ProductDTO> {
        let url = `/api/v1/products/${productId}`;
        return this.http.get<ProductDTO>(url);
    }
}