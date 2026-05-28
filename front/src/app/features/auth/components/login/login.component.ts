import { Component } from "@angular/core";
import { AuthService } from "../../services/auth.service";
import { FormBuilder, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { SessionService } from "src/app/services/session.service";
import { AuthSuccess } from "../../interfaces/AuthSuccess.interface";
import { HttpErrorResponse } from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public errorMessage: string = '';

  public form = this.fb.group({
    identifier: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService
  ) {}

  public submit(): void {
    if (this.form.invalid) return;

    this.authService.login(this.form.value as any).subscribe({
      next: (response: AuthSuccess) => {
        this.sessionService.logInWithToken(response.token);
        this.router.navigate(['/feed']);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 400 || error.status === 401) {
          this.errorMessage = "Identifiants ou mot de passe incorrect.";
        } else {
          this.errorMessage = "Une erreur inattendue est survenue.";
        }
      }
    });
  }
}