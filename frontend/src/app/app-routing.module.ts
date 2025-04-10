import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginSigninComponent } from './components/user/login-signin/login-signin.component';
import { FavoriteProductListComponent } from './components/user/favorite-product-list/favorite-product-list.component';
import { ProductDetailComponent } from './components/product/product-detail/product-detail.component';

const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'login', component: LoginSigninComponent},
  {path: 'profile', component: FavoriteProductListComponent},
  {path: 'product/:id', component: ProductDetailComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
