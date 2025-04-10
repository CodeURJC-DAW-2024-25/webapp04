import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ProductDTO } from '../dtos/product.dto';
import { ChatDTO } from '../dtos/chat.dto';

@Injectable({
    providedIn: 'root'
})

export class ChatService {
    constructor(private http: HttpClient) { }
    
    getUserChats(): Observable<{ content: ChatDTO[] }> {
        const url = `/api/v1/chats`;
        return this.http.get<{content: ChatDTO[]}>(url);
    }

    getBuyerChats(): Observable<{ content: ChatDTO[] }> {
        const url = '/api/v1/chats?role=buyer';
        return this.http.get<ChatDTO[]>(url).pipe(
          map((data) => ({ content: data }))
        );
      }

      getSellerChats(): Observable<{ content: ChatDTO[] }> {
        const url = '/api/v1/chats?role=seller';
        return this.http.get<ChatDTO[]>(url).pipe(
          map((data) => ({ content: data }))
        );
      }

    getChatById(chatId: number): Observable<ChatDTO> {
        const url = `/api/v1/chats/${chatId}`;
        return this.http.get<ChatDTO>(url);
    }       
    
    sendChatMessage(chatId: number, message: { text: string }): Observable<ChatDTO> {
      const url = `/api/v1/chats/${chatId}/messages`;
      return this.http.post<ChatDTO>(url, message);
    }

    sellProduct(chatId: number, userId: number): Observable<any> {
      const url = `/api/v1/users/${userId}/purchases`;
      const body = { chatID: chatId };
      return this.http.post(url, body);
    }
}