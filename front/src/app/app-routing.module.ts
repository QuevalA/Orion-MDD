import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {TopicsListComponent} from "./features/topics/topics-list/topics-list.component";
import {PostsListComponent} from "./features/posts/posts-list/posts-list.component";
import {UserDetailComponent} from "./features/user/user-detail/user-detail.component";
import {PostDetailComponent} from "./features/posts/post-detail/post-detail.component";
import {PostCreationComponent} from "./features/posts/post-creation/post-creation.component";
import {RegisterComponent} from "./features/auth/register/register.component";
import {LoginComponent} from "./features/auth/login/login.component";
import {NotFoundComponent} from "./pages/not-found/not-found.component";

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes

//ToDo: edit "posts/detail" route to "posts/:id" once ready to handle dynamic data
const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'topics',
    component: TopicsListComponent
  },
  {
    path: 'posts',
    component: PostsListComponent
  },
  {
    path: 'posts/create',
    component: PostCreationComponent
  },
  {
    path: 'posts/detail',
    component: PostDetailComponent
  },
  {
    path: 'user',
    component: UserDetailComponent
  },
  {
    path: '**', // wildcard
    component: NotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
