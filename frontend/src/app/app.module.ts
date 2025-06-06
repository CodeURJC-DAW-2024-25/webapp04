import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
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
import { NewProductComponent } from './components/product/new-product/new-product.component';
import { ReportFormComponent } from './components/reports/report-form/report-form.component';
import { ReportListComponent } from './components/reports/report-list/report-list.component';
import { ReviewFormComponent } from './components/reviews/review-form/review-form.component';
import { ReviewListComponent } from './components/reviews/review-list/review-list.component';
import { EditProfileComponent } from './components/user/edit-profile/edit-profile.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { UserStatsChartComponent } from './components/user/user-stats-chart/user-stats-chart.component';
import { ProfileReviewsComponent } from './components/reviews/profile-reviews/profile-reviews.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';

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
    NewProductComponent,
    ReportFormComponent,
    ReportListComponent,
    ReviewFormComponent,
    ReviewListComponent,
    EditProfileComponent,
    UserStatsChartComponent,
    ProfileReviewsComponent,
    AccessDeniedComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    ReactiveFormsModule,
    NgxChartsModule,
    BrowserAnimationsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
