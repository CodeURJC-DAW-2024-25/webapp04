import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginSigninComponent } from './components/user/login-signin/login-signin.component';
import { ProductDetailComponent } from './components/product/product-detail/product-detail.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { NewProductComponent } from './components/product/new-product/new-product.component';
import { ChatComponent } from './components/chats/chat/chat.component';
import { ConfirmSaleComponent } from './components/chats/confirm-sale/confirm-sale.component';
import { ProfilePreviewComponent } from './components/user/profile-preview/profile-preview.component';
import { ProfileComponent } from './components/user/profile/profile.component';

const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'login', component: LoginSigninComponent},
  {path: 'profileFavorites', component: ProductListComponent},
  {path: 'profile/me', component: ProfileComponent},
  {path: 'profile/:id', component: ProfileComponent},
  {path: 'adminProfile', component: ProfileComponent},
  {path: 'product/:id', component: ProductDetailComponent},
  {path: 'chats', component: ChatComponent},
  {path: 'confirm-sale/:chatId', component: ConfirmSaleComponent},
  {path: 'products', component: ProductListComponent},
  {path: 'newProduct', component: NewProductComponent},
  {path: 'editProduct/:id', component: NewProductComponent},
  {path: 'chats/:id', component: ChatComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
