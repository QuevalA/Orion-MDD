import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { PostsListComponent } from './features/posts/posts-list/posts-list.component';
import { PostDetailComponent } from './features/posts/post-detail/post-detail.component';
import { PostCreationComponent } from './features/posts/post-creation/post-creation.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LoginComponent } from './features/auth/login/login.component';
import { TopicsListComponent } from './features/topics/topics-list/topics-list.component';
import { UserDetailComponent } from './features/user/user-detail/user-detail.component';
import { HeaderComponent } from './shared/header/header.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';

@NgModule({
  declarations: [AppComponent, HomeComponent, PostsListComponent, PostDetailComponent, PostCreationComponent, RegisterComponent, LoginComponent, TopicsListComponent, UserDetailComponent, HeaderComponent, NotFoundComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
