import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ChatService } from '../../../services/chat.service';
import { UserService } from '../../../services/user.service';
import { ProductDTO } from '../../../dtos/product.dto';
import { UserBasicDTO } from '../../../dtos/user.basic.dto';


@Component({
  selector: 'app-confirm-sale',
  templateUrl: './confirm-sale.component.html',
  styleUrls: ['./confirm-sale.component.css']
})
export class ConfirmSaleComponent implements OnInit {
  product: ProductDTO | null = null;
  buyer: UserBasicDTO | null = null;
  currentUserId: number | null = null;
  chatId: number | null = null;

  constructor(
    private router: Router,
    private chatService: ChatService,
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
    const chatIdParam = this.activatedRoute.snapshot.params['chatId'];

    this.userService.getUser().subscribe(user => {
      this.currentUserId = user.id;
    });

    if (chatIdParam) {
      this.chatId = +chatIdParam;
      this.chatService.getChatById(this.chatId).subscribe(chat => {
        this.product = chat.product;
        this.buyer = chat.userBuyer;
      });
    }
  }

  confirmSale(): void {
    if (this.chatId && this.currentUserId) {
      this.chatService.sellProduct(this.chatId, this.currentUserId).subscribe(() => {
        this.router.navigate(['/chats']);
      });
    }
  }

  cancelSale(): void {
    this.router.navigate(['/chats']);
  }
}
