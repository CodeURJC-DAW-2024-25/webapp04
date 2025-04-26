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
import { EditProfileComponent } from './components/user/edit-profile/edit-profile.component';
import { ReportListComponent } from './components/reports/report-list/report-list.component';
import { AuthGuard } from './auth/auth.guard';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'login', component: LoginSigninComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'profile/:id', component: ProfileComponent },
  { path: 'product/:id', component: ProductDetailComponent },
  { path: 'products', component: ProductListComponent },

  // Protected with AuthGuard
  { path: 'editProfile', component: EditProfileComponent, canActivate: [AuthGuard], data: { roles: ['USER','ADMIN'] } },
  { path: 'newProduct', component: NewProductComponent, canActivate: [AuthGuard], data: { roles: ['USER'] } },
  { path: 'editProduct/:id', component: NewProductComponent, canActivate: [AuthGuard], data: { roles: ['USER'] } },
  { path: 'chats', component: ChatComponent, canActivate: [AuthGuard], data: { roles: ['USER'] } },
  { path: 'chats/:id', component: ChatComponent, canActivate: [AuthGuard], data: { roles: ['USER'] } },
  { path: 'confirm-sale/:chatId', component: ConfirmSaleComponent, canActivate: [AuthGuard], data: { roles: ['USER'] } },

  { path: 'adminProfile', component: ProfileComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'reports', component: ReportListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },

  // Denied access page
  { path: 'access-denied', component: AccessDeniedComponent }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
