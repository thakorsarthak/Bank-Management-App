import { Component } from '@angular/core';

import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PublicHeaderComponent } from '../shared/header/public-header/public-header.component';
import { PublicFooterComponent } from '../shared/footer/public-footer/public-footer.component';


@Component({
  selector: 'app-core-component',
  imports: [PublicHeaderComponent, PublicFooterComponent, RouterOutlet, CommonModule],
  templateUrl: './core-component.component.html',
  styleUrl: './core-component.component.css'
})
export class CoreComponentComponent {

  showPublicHeaderFooter = true;

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        const childRoute = this.getLastChild(this.activatedRoute);
        const path = childRoute.snapshot.routeConfig?.path || '';

        console.log('Path:', path); // Debug log

        // Hide for loginPage and openAccount
        this.showPublicHeaderFooter = !['login', 'openAccount'].includes(
          path
        );
      });
  }

  private getLastChild(route: ActivatedRoute): ActivatedRoute {
    while (route.firstChild) {
      route = route.firstChild;
    }
    return route;
  }
  }

