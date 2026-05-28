import { Component } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { AuthService } from "../../services/auth.service";
import { Router } from "@angular/router";
import { SessionService } from "src/app/services/session.service";
import { AuthSuccess } from "../../interfaces/AuthSuccess.interface";
import { HttpErrorResponse } from "@angular/common/http";
import { ApiError } from "src/app/interfaces/api-error.interface";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})

export class RegisterComponent {

  public errorMessage: string = '';

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', [Validators.required, 
                    Validators.minLength(8),
                    /**
                     * Valide le mot de passe côté client avant envoi au back-end.
                     * Règles : 8 caractères min, 1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial.
                    */ 
                    Validators.pattern(/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])[A-Za-z\d\S]{8,}$/)  
    ]]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService) { }

  public submit(): void {
    if (this.form.invalid) return;
      
    this.authService.register(this.form.value as any).subscribe({
      next: (response: AuthSuccess) => {
        this.sessionService.logInWithToken(response.token);
        this.router.navigate(['/feed']);
      },
      error: (error: HttpErrorResponse) => {
        const apiError = error.error as ApiError;
        if (error.status === 409) {
          this.errorMessage = apiError.message;
        }else if (error.status === 400) {
          this.errorMessage = apiError.message;
        } else {
          this.errorMessage = "Une erreur inattendue est survenue.";
        }
      }
    });
  }
}