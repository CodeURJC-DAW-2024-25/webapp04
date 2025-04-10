import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginSigninComponent } from './components/user/login-signin/login-signin.component';
import { FavoriteProductListComponent } from './components/user/favorite-product-list/favorite-product-list.component';
import { ProductDetailComponent } from './components/product/product-detail/product-detail.component';
import { ChatComponent } from './components/chats/chat/chat.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { ConfirmSaleComponent } from './components/chats/confirm-sale/confirm-sale.component';

const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'login', component: LoginSigninComponent},
  {path: 'profile', component: FavoriteProductListComponent},
  {path: 'product/:id', component: ProductDetailComponent},
  {path: 'chats', component: ChatComponent},
  {path: 'confirm-sale/:chatId', component: ConfirmSaleComponent},
  { path: 'products', component: ProductListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
