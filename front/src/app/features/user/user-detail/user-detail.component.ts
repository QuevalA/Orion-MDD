import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {

  // Placeholder data for subscribed topics
  subscribedTopics: any[] = [
    {name: 'Technology', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
    {name: 'Programming', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
    {name: 'Cyber security', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
  ];

  isSmallScreen: boolean | undefined;

  constructor(private breakpointObserver: BreakpointObserver) {
    this.breakpointObserver.observe([Breakpoints.Small, Breakpoints.XSmall]).subscribe(result => {
      this.isSmallScreen = result.matches;
    });
  }

  ngOnInit(): void {
  }
}
