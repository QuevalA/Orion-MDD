import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {PostsService} from "../../../services/posts.service";

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.scss']
})
export class PostsListComponent implements OnInit {

  blogPosts: any[] = [];
  sortByOption: string = 'date';
  isSmallScreen: boolean | undefined;

  constructor(
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private postsService: PostsService
  ) {
    this.breakpointObserver.observe([Breakpoints.Small, Breakpoints.XSmall]).subscribe(result => {
      this.isSmallScreen = result.matches;
    });
  }

  ngOnInit(): void {
    this.fetchPosts();
  }

  createPost() {
    this.router.navigate(['/posts/create']);
  }

  sortPosts() {

    if (this.sortByOption === 'date') {
      this.blogPosts.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
    } else if (this.sortByOption === 'title') {
      this.blogPosts.sort((a, b) => a.title.localeCompare(b.title));
    } else if (this.sortByOption === 'author') {
      this.blogPosts.sort((a, b) => a.postAuthorUsername.localeCompare(b.postAuthorUsername));
    }

  }

  fetchPosts() {
    this.postsService.getPostsBySubscriptions().subscribe(
      (posts: any[]) => {
        this.blogPosts = posts;
        this.sortPosts();
      },
      error => {
        console.error('Error fetching posts:', error);
      }
    );
  }
}
