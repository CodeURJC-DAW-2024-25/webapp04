import { Component, ViewChild, ElementRef } from '@angular/core';
import { ChatDTO } from '../../../dtos/chat.dto';
import { ChatService } from '../../../services/chat.service';
import { UserService } from '../../../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';

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
  currentUser: any;
  @ViewChild('messagesContainer') messagesContainerRef!: ElementRef;

  constructor(private router: Router, private route: ActivatedRoute, private chatService: ChatService, private userService: UserService) { }

  ngOnInit(): void {
    this.refreshChatPeriodically();
    // Check if user is logged in
    this.userService.getUser().subscribe({
      next: (user) => {
        this.currentUser = user;
        console.log('User is logged in', user);
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });

    // Get chats of buyer
    this.chatService.getBuyerChats().subscribe((response) => {
      this.buyChats = response.content.reverse();
    });

    // Get chats of seller
    this.chatService.getSellerChats().subscribe((data: { content: ChatDTO[] }) => {
      this.sellChats = data.content.reverse();
    });

    // Navigate to the selected chat (or null if '')
    this.route.params.subscribe(params => {
      const chatId = +params['id'];
      if (chatId) {
        this.goToChat(chatId);
      } else {
        this.currentChat = null;
      }
    });
  }

  refreshChatPeriodically() {
    setInterval(() => {
      if (this.currentChat) {
        this.chatService.getChatById(this.currentChat.id).subscribe({
          next: (chat) => {
            if (this.currentChat) {
              this.currentChat.messages = chat.messages; 
              this.scrollToBottom();
            }
          }
        });
      }
    }, 3000);
  }

  ngAfterViewInit(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    setTimeout(() => {
      const messagesContainer = this.messagesContainerRef?.nativeElement;
      if (messagesContainer) {
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
      }
    }, 0);
  }

  goToChat(chatId: number): void {
    // Navigate to the chat page only if it's not already the active chat
    if (this.currentChat?.id !== chatId) {
      this.chatService.getChatById(chatId).subscribe({
        next: (chat) => {
          this.router.navigate(['/chats', chatId]);
          this.currentChat = chat;
          this.currentChatName = `${chat.userBuyer.name} - ${chat.product.name}`;
          this.scrollToBottom();
        },
        error: (err) => {
          console.error('Error fetching chat:', err);
        }
      });
    }
  }

  // Send a message in the current chat
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
          name: this.currentUser.name,
          image: this.currentUser.hasImage
            ? `https://localhost:8443/user/${this.currentUser.id}/image`
            : 'default-image-url',
          hasImage: this.currentUser.hasImage,
          roles: this.currentUser.roles,
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

  // Go to the confirm sale page with the chat ID
  goToConfirmSale(chatId: number): void {
    this.router.navigate(['/confirm-sale', chatId]);
  }

  trackByMessageId(index: number, message: any): any {
    return message.id;
  }
  

}
