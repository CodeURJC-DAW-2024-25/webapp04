import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserBasicDTO } from '../dtos/user.basic.dto';


@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) { }
    
    getUser(): Observable<UserBasicDTO> {
        let url = '/api/v1/users/2';
        return this.http.get<UserBasicDTO>(url);
    }
}