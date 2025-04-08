import { Component} from '@angular/core';
import { ChatDTO } from '../../dtos/chat.dto';
import { ChatService } from '../../services/chat.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  buyChats: ChatDTO[] = [];
  sellChats: ChatDTO[] = [];
  currentChat: ChatDTO | null = null;
  currentChatName: string = '';
  newMessage: string = '';

  constructor(private chatService: ChatService) { }

  ngOnInit(): void {
    // Get chats of buyer
    this.chatService.getBuyerChats().subscribe((response) => {
        this.buyChats = response.content
    }
    );

    // Get chats of seller
    this.chatService.getSellerChats().subscribe((data: { content: ChatDTO[] }) => {
      this.sellChats = data.content;
    });
  }

  goToChat(chatId: number): void {
    this.chatService.getChatById(chatId).subscribe(chat => {
      this.currentChat = chat;
      this.currentChatName = `${chat.userBuyer.name} - ${chat.product.name}`;
    });
  }

  sendMessage(): void {
  }

  sellProduct(chatId: number): void {
  }
}
