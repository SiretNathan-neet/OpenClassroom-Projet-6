import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {

  public isMenuOpen = false;

  constructor(
    private sessionService: SessionService,
    private router: Router
  ) {}

  public toggleMobileMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  public logOut(): void {
    this.sessionService.logOut();
    this.isMenuOpen = false;
    this.router.navigate(['/login']);
  }
}