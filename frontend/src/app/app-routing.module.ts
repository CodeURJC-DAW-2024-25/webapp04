import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginSigninComponent } from './components/user/login-signin/login-signin.component';
import { FavoriteProductListComponent } from './components/user/favorite-product-list/favorite-product-list.component';
import { ChatComponent } from './components/chat/chat.component';
import { ProfilePreviewComponent } from './components/user/profile-preview/profile-preview.component';
import { ProfileComponent } from './components/user/profile/profile.component';

const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'login', component: LoginSigninComponent},
  {path: 'profile/me', component: ProfileComponent},
  {path: 'profile/:id', component: ProfileComponent},
  {path: 'adminProfile', component: ProfileComponent},
  {path: 'chats', component: ChatComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
