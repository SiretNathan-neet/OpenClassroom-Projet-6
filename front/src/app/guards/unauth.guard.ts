import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router"; 
import { SessionService } from "../services/session.service";

@Injectable({providedIn: 'root'})
export class UnauthGuard implements CanActivate {

  constructor( 
    private router: Router,
    private sessionService: SessionService,
  ) {
  }

  /**
   * Empêche un utilisateur déjà connecté d'accéder aux pages publiques (login, register) et le redirige vers le feed.
   * L'utilisateur peut toujours accéder à ces pages en se déconnectant.
   */
  public canActivate(): boolean {
    if (this.sessionService.isLogged) {
      this.router.navigate(['feed']);
      return false;
    }
    return true;
  }
}