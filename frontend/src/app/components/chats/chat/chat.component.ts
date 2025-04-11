import { Component, ViewChild, ElementRef } from '@angular/core';
import { ChatDTO } from '../../../dtos/chat.dto';
import { ChatService } from '../../../services/chat.service';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';

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
  @ViewChild('messagesContainer') messagesContainerRef!: ElementRef;

  constructor(private router: Router,  private chatService: ChatService,  private userService: UserService) { }

  ngOnInit(): void {
    // Get chats of buyer
    this.chatService.getBuyerChats().subscribe((response) => {
        this.buyChats = response.content
    });

    // Get chats of seller
    this.chatService.getSellerChats().subscribe((data: { content: ChatDTO[] }) => {
      this.sellChats = data.content;
    });
  }

  ngAfterViewInit(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
  setTimeout(() => {
    const messagesContainer = this.messagesContainerRef.nativeElement;
    if (messagesContainer) {
      messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }
  }, 0); 
}

  goToChat(chatId: number): void {
    this.chatService.getChatById(chatId).subscribe(chat => {
      this.currentChat = chat;
      this.currentChatName = `${chat.userBuyer.name} - ${chat.product.name}`;
      this.scrollToBottom();
    });
  }

  sendMessage() {
    if (this.currentChat && this.newMessage.trim()) {
      const chatId = this.currentChat.id;
      const message = { text: this.newMessage };
      const formattedDate = new Intl.DateTimeFormat('es-ES', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false,
      }).format(new Date()).replace(',', '');

      const newMessage = {
        id: 0,
        sender: {
          id: 2,
          name: 'Pedro',
          image: 'https://localhost:8443/user/2/image',
          hasImage: false,
          roles: ['USER'],
        },
        message: this.newMessage,
        sentAt: formattedDate,
      };

      this.currentChat.messages.push(newMessage);
      this.chatService.sendChatMessage(chatId, message).subscribe(() => {
        this.newMessage = '';
        this.scrollToBottom();
      });
    }
  }

  goToConfirmSale(chatId: number): void {
    this.router.navigate(['/confirm-sale', chatId]);
  }

}
