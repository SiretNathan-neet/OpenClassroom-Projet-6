import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthService } from '../features/auth/services/auth.service';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public isLogged = false;
  public user: User | undefined;

  /**
   * BehaviorSubject permettant aux composants de réagir 
   * aux changements d'état de connexion en temps réel.
   */
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(user: User): void {
    this.user = user;
    this.isLogged = true;
    this.next();
  }

  /**
   * Connecte l'utilisateur à partir d'un token JWT.
   * Stocke le token en localStorage pour faire persister la session.
   */
  public logInWithToken(token: string): void {
    localStorage.setItem('token', token);
    this.isLogged = true;
    this.next();
  }

  public logOut(): void {
    localStorage.removeItem('token');
    this.user = undefined;
    this.isLogged = false;
    this.next();
  }

  /** Notifie tous les observateurs du changement d'état de connexion. */
  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }
}
