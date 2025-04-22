import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserBasicDTO } from '../dtos/user.basic.dto';
import { UserDTO } from '../dtos/user.dto';
import { ProductDTO } from '../dtos/product.dto';
import { map, tap } from 'rxjs/operators';
import { EditUserDTO } from '../dtos/edit.user.dto';


@Injectable({
    providedIn: 'root'
})
export class UserService {
    private userSubject = new BehaviorSubject<UserBasicDTO | undefined>(undefined);
    userChanges$ = this.userSubject.asObservable();

    constructor(private http: HttpClient) { }

    getUser(): Observable<UserBasicDTO> {
        let url = '/api/v1/users/me';
        return this.http.get<UserBasicDTO>(url).pipe(
            tap(user => this.userSubject.next(user)) 
        );
    }

    getBasicUserById(userId: number): Observable<UserBasicDTO> {
        userId = userId || 0;
        let url = `/api/v1/users/${userId}/basic`;
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

    checkHasBoughtUser(buyerId: number, sellerId: number): Observable<boolean> {
        let url = `/api/v1/users/${buyerId}/purchases?sellerId=${sellerId}`;
        return this.http.get<ProductDTO[]>(url).pipe(
            map(response => { return response.length > 0 })
        );
    }

    checkHasBoughtProduct(buyerId: number, productId: number): Observable<boolean> {
        let url = `/api/v1/users/${buyerId}/purchases?productId=${productId}`;
        return this.http.get<ProductDTO[]>(url).pipe(
            map(response => { return response.length > 0 })
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
        return new Observable<boolean>((observer) => {
            this.http.get<{ totalElements: number }>(
                `/api/v1/users/${userId}/favorites`
            ).subscribe({
                next: (response) => {
                    const total = response.totalElements;
    
                    if (total === 0) {
                        observer.next(false);
                        observer.complete();
                        return;
                    }
    
                    this.http.get<{ content: ProductDTO[] }>(
                        `/api/v1/users/${userId}/favorites?size=${total}`
                    ).subscribe({
                        next: (res) => {
                            const isFav = res.content.some(p => p.id === productId);
                            observer.next(isFav);
                            observer.complete();
                        },
                        error: (err) => observer.error(err)
                    });
                },
                error: (err) => observer.error(err)
            });
        });
    }    

    logout(): Observable<{ status: string }> {
        let url = '/api/v1/auth/logout';
        return this.http.post<{ status: string }>(url, {});
    }

    updateUser(user: EditUserDTO, userId: number): Observable<any> {
        const url = `/api/v1/users/${userId}`;
        return this.http.put<any>(url, user).pipe(
            tap(() => this.refreshUser()) // Refresh user after update
        );
    }
    
    updateProfileImage(image: FormData, userId: number): Observable<any> {
        const url = `/api/v1/users/${userId}/images`;
        return this.http.post(url, image).pipe(
            tap(() => this.refreshUser()) // Refresh user after image update
        );
    }
    
    private refreshUser(): void {
        // Re-fetch the updated user details and broadcast the changes
        this.getUser().subscribe();
    }

    getProfileImageUrl(userId: number): string {
        const timestamp = new Date().getTime();
        return `/api/v1/users/${userId}/images?t=${timestamp}`;
      }
}