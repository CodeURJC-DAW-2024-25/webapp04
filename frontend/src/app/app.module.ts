import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { TopProductListComponent } from './components/product/top-product-list/top-product-list.component';
import { OtherProductListComponent } from './components/product/other-product-list/other-product-list.component';
import { ProfilePreviewComponent } from './components/user/profile-preview/profile-preview.component';
import { ProductDetailComponent } from './components/product/product-detail/product-detail.component';
import { HeaderComponent } from './components/header/header.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginSigninComponent } from './components/user/login-signin/login-signin.component';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';

import { ProfileComponent } from './components/user/profile/profile.component';
import { ChatComponent } from './components/chats/chat/chat.component';
import { ConfirmSaleComponent } from './components/chats/confirm-sale/confirm-sale.component';
import { ReportFormComponent } from './components/reports/report-form/report-form.component';
import { ReviewFormComponent } from './components/reviews/review-form/review-form.component';
import { ReviewListComponent } from './components/reviews/review-list/review-list.component';

@NgModule({
  declarations: [
    AppComponent,
    TopProductListComponent,
    OtherProductListComponent,
    ProfilePreviewComponent,
    ProductDetailComponent,
    HeaderComponent,
    HomePageComponent,
    LoginSigninComponent,
    NavbarComponent,
    ProductListComponent,
    ChatComponent,
    ConfirmSaleComponent,
    ProfileComponent,
    ReportFormComponent,
    ReviewFormComponent,
    ReviewListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
