import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router"; 
import { SessionService } from "../services/session.service";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

  constructor( 
    private router: Router,
    private sessionService: SessionService,
  ) {
  }

  /**
   * Vérifie la présence d'un token JWT avant d'accéder à une route protégée.
   * Restaure également l'état de session si la page a été rafraîchie
   * et que le token est toujours présent en localStorage.
   * Redirige vers la page de connexion si aucun token n'est trouvé.
   */
  public canActivate(): boolean {
    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['login']);
      return false;
    }

    if (!this.sessionService.isLogged) {
      this.sessionService.logInWithToken(token);
    }
    
    return true;
  }
}