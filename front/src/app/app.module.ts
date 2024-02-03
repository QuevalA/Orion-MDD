import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './pages/home/home.component';
import {PostsListComponent} from './features/posts/posts-list/posts-list.component';
import {PostDetailComponent} from './features/posts/post-detail/post-detail.component';
import {PostCreationComponent} from './features/posts/post-creation/post-creation.component';
import {RegisterComponent} from './features/auth/register/register.component';
import {LoginComponent} from './features/auth/login/login.component';
import {TopicsListComponent} from './features/topics/topics-list/topics-list.component';
import {UserDetailComponent} from './features/user/user-detail/user-detail.component';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatCardModule} from "@angular/material/card";
import {MatSelectModule} from "@angular/material/select";
import {MatListModule} from "@angular/material/list";
import {HttpClientModule} from "@angular/common/http";
import {MatMenuModule} from "@angular/material/menu";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MainNavComponent} from './shared/main-nav/main-nav.component';
import {LayoutModule} from '@angular/cdk/layout';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PostsListComponent,
    PostDetailComponent,
    PostCreationComponent,
    RegisterComponent,
    LoginComponent,
    TopicsListComponent,
    UserDetailComponent,
    NotFoundComponent,
    MainNavComponent,
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    FormsModule,
    MatGridListModule,
    MatCardModule,
    MatSelectModule,
    MatListModule,
    MatMenuModule,
    MatSidenavModule,
    MatToolbarModule,
    LayoutModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {
}
