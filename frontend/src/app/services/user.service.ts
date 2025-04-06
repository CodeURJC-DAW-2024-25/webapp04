import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserBasicDTO } from '../dtos/user.basic.dto';
import { map } from 'rxjs/operators';


@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) { }
    
    //TODO: Poner un endpoint para las imagenes en la API
    getUser(): Observable<UserBasicDTO> {
        let url = '/api/v1/users/me';
        return this.http.get<UserBasicDTO>(url);
    }

    loginUser(username: string, password: string): Observable<{status: string}> {
        let url = '/api/v1/auth/login';
        return this.http.post<{status: string}>(url, {username, password});
    }

    registerUser(name: string, mail: string, password: string, repeatPassword: string): Observable<boolean> {
        let url = '/api/v1/users/signin';
        return this.http.post<{id: number}>(url, {name, mail, password, repeatPassword}).pipe(
            map(response => {return response.id !== undefined})
        );
    }
}