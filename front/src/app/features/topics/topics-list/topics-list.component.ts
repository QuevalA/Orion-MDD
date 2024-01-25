import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";

@Component({
  selector: 'app-topics-list',
  templateUrl: './topics-list.component.html',
  styleUrls: ['./topics-list.component.scss']
})
export class TopicsListComponent implements OnInit {

  // Placeholder
  topics: any[] = [
    {name: 'Technology', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
    {name: 'Science', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
    {name: 'Programming', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
    {name: 'Hosting', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
    {name: 'Web', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
    {name: 'Dev ops', description: 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit'},
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
