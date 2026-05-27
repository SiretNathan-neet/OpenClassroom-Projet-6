import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { SessionService } from "../services/session.service";
import { catchError, throwError } from "rxjs";

@Injectable({ providedIn: 'root' })
export class JwtInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private sessionService: SessionService
  ) {}

  /**
   * Intercepte toutes les requêtes HTTP sortantes pour y injecter le token JWT.
   * Gère également les réponses 401 (token expiré ou invalide) en déconnectant
   * automatiquement l'utilisateur et en le redirigeant vers la page de login.
   */
  public intercept(request: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('token');

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.sessionService.logOut();
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}