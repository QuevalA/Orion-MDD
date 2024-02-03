import {Component} from '@angular/core';
import {filter} from "rxjs";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  showMainNav: boolean = true;

  constructor(private router: Router, private route: ActivatedRoute) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.showMainNav = this.shouldShowMainNav();
    });
  }

  shouldShowMainNav(): boolean {
    // Logic to determine if main nav should be shown based on the current route
    const url = this.router.url;
    return !(url.startsWith('/posts') ||
      url === '/topics' ||
      url === '/user' ||
      url === '/posts/create');
  }
}
