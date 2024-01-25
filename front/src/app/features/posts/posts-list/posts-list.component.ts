import {Component, NgIterable, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.scss']
})
export class PostsListComponent implements OnInit {

  //Temporary placeholder
  blogPosts = [
    {
      title: 'First Blog Post',
      date: new Date('2022-01-01'),
      author: 'John Doe',
      content: 'Lorem ipsum dolor sit amet, elit. ...',
    },
    {
      title: 'Second Blog Post',
      date: new Date('2022-02-15'),
      author: 'Jane Smith',
      content: 'Sed do eiusmod tempor do eiusmod tempor incididunt ut labore et dolore incididunt ut labore et dolore magna aliqua. ...',
    },
    {
      title: 'Blog Post #3',
      date: new Date('2022-02-15'),
      author: 'Jane Doe',
      content: 'Sed do eiusmod tempor incididunt ut do eiusmod tempor incididunt ut labore et dolore labore et dolore magna aliqua. ...',
    },
    {
      title: 'Blog Post #4',
      date: new Date('2022-02-15'),
      author: 'John Smith',
      content: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ...',
    },
    {
      title: 'Blog Post #5',
      date: new Date('2022-02-15'),
      author: 'Joe Shmoe',
      content: 'Sed do eiusmod tempor do eiusmod tempor incididunt ut labore et dolore incididunt ut labore et dolore magna aliqua. ...',
    },
  ];

  //Temporary placeholder
  sortByOption: string = 'date';

  isSmallScreen: boolean | undefined;

  constructor(private router: Router, private breakpointObserver: BreakpointObserver) {
    this.breakpointObserver.observe([Breakpoints.Small, Breakpoints.XSmall]).subscribe(result => {
      this.isSmallScreen = result.matches;
    });
  }

  ngOnInit(): void {
  }

  createPost() {
    console.log('Navigating to create post page...');
    this.router.navigate(['/posts/create']);
  }

  sortPosts() {
  }
}
