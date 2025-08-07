import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { PublicFooterComponent } from './core-component/core-component/public-footer/public-footer.component';
import { PublicHeaderComponent } from "./core-component/core-component/public-header/public-header.component";
import { PublicMainComponent } from "./component/layout/public-main/public-main.component";
import { OpenAccountComponent } from './core-component/core-component/open-account/open-account.component';





@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ButtonModule, PublicFooterComponent, PublicHeaderComponent, PublicMainComponent, OpenAccountComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
