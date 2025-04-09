import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginSigninComponent } from './components/user/login-signin/login-signin.component';
import { FavoriteProductListComponent } from './components/user/favorite-product-list/favorite-product-list.component';
import { ChatComponent } from './components/chats/chat/chat.component';
import { ConfirmSaleComponent } from './components/chats/confirm-sale/confirm-sale.component';

const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'login', component: LoginSigninComponent},
  {path: 'profile', component: FavoriteProductListComponent},
  {path: 'chats', component: ChatComponent},
  { path: 'confirm-sale/:chatId', component: ConfirmSaleComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
