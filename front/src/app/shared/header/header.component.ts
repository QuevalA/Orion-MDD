import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  showHeaderButtons: boolean = true;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.url.subscribe(urlSegments => {
      this.showHeaderButtons = !urlSegments.find(segment => segment.path === 'register' || segment.path === 'login');
    });
  }
}
