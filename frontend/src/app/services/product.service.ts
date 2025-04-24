import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO } from '../dtos/product.dto';
import { ProductBasicDTO } from '../dtos/product.basic.dto';
import { PurchaseDTO } from '../dtos/purchase.dto';


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

    getProductsByName(searchTerm: string, pageNumber: number = 0): Observable<{ content: ProductDTO[], last: boolean }> {
        const url = `/api/v1/products?name=${encodeURIComponent(searchTerm)}&page=${pageNumber}`;
        return this.http.get<{ content: ProductDTO[], last: boolean }>(url);
    }

    createProduct(productData: any): Observable<{id: number}> {
        return this.http.post<any>(`/api/v1/products`, productData);
    }

    updateProduct(productData: any, productId: number | undefined): Observable<{id: number}> {
        return this.http.put<any>(`/api/v1/products/${productId}`, productData);
    }

    deleteProduct(productId: number): Observable<any> {
        return this.http.delete(`/api/v1/products/${productId}`);
    }

    addImage(productId: number, formData: FormData): Observable<any> {
        return this.http.post(`/api/v1/products/${productId}/images`, formData);
    }

    deleteImage(productId: number, imageId: number): Observable<any> {
        return this.http.delete(`/api/v1/products/${productId}/images/${imageId}`);
    }

    getPurchases(userId: number, role: string): Observable<PurchaseDTO[]> {
        let url = `/api/v1/users/${userId}/purchases?role=${encodeURIComponent(role)}`;
        return this.http.get<PurchaseDTO[]>(url);
    }
      
}