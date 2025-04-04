import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TopProductListComponent } from './components/product/top-product-list/top-product-list.component';
import { OtherProductListComponent } from './components/product/other-product-list/other-product-list.component';
import { ProfilePreviewComponent } from './components/user/profile-preview/profile-preview.component';
import { ProductDetailComponent } from './components/product/product-detail/product-detail.component';
import { HeaderComponent } from './components/header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    TopProductListComponent,
    OtherProductListComponent,
    ProfilePreviewComponent,
    ProductDetailComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
