import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserBasicDTO } from '../dtos/user.basic.dto';
import { UserDTO } from '../dtos/user.dto';
import { ProductDTO } from '../dtos/product.dto';
import { map } from 'rxjs/operators';


@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) { }

    getUser(): Observable<UserBasicDTO> {
        let url = '/api/v1/users/me';
        return this.http.get<UserBasicDTO>(url);
    }

    getUserById(userId: number): Observable<UserDTO> {
        userId = userId || 0;  
        let url = `/api/v1/users/${userId}`;
        return this.http.get<UserDTO>(url);
    }

    loginUser(username: string, password: string): Observable<{ status: string }> {
        let url = '/api/v1/auth/login';
        return this.http.post<{ status: string }>(url, { username, password });
    }

    registerUser(name: string, mail: string, password: string, repeatPassword: string): Observable<boolean> {
        let url = '/api/v1/users/signin';
        return this.http.post<{ id: number }>(url, { name, mail, password, repeatPassword }).pipe(
            map(response => { return response.id !== undefined })
        );
    }

    addFavorite(userId: number, productId: number): Observable<void> {
        return this.http.post<void>(`/api/v1/users/${userId}/favorites`, { productId: productId });
    }

    getFavorites(userId: number, page: number): Observable<{ content: ProductDTO[], last: boolean }> {
        return this.http.get<{ content: ProductDTO[], last: boolean }>(
            `/api/v1/users/${userId}/favorites?page=${page}`
        );
    }

    getAllFavorites(userId: number): Observable<ProductDTO[]> {
        const pageSize = 1000; 
        return this.http.get<{ content: ProductDTO[] }>(
            `/api/v1/users/${userId}/favorites?size=${pageSize}`
        ).pipe(
            map(response => response.content)
        );
    }

    deleteFavorite(userId: number, productId: number): Observable<void> {
        return this.http.delete<void>(
            `/api/v1/users/${userId}/favorites/${productId}`
        );
    }

    isFavorite(userId: number, productId: number): Observable<boolean> {
        let totalElements = 0;
        this.http.get<{totalElements: number}>(
            `/api/v1/users/${userId}/favorites`
        ).subscribe({
            next: (response) => {
                totalElements = response.totalElements;
            }
        });

        if (totalElements > 0) {
            let ids: number[] = [];
            return this.http.get<{ content: ProductDTO[], last: boolean }>(
                `/api/v1/users/${userId}/favorites?size=${totalElements}`
            ).pipe(
                map(response => ids.push(...response.content.map(product => product.id))),
                map(() => ids.includes(productId)),
            );
        } else {
            return new Observable<boolean>(observer => {
                observer.next(false);
                observer.complete();
            });
        }
            
    }

}