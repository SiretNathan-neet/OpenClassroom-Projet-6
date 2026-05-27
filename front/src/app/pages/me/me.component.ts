import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { SessionService } from 'src/app/services/session.service';
import { User } from 'src/app/interfaces/user.interface';
import { Topic } from 'src/app/interfaces/topic.interface';
import { TopicService } from 'src/app/services/topic.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {

  public onError = false;
  public onSuccess = false;
  public user: User | undefined;
  public subscriptions: Topic[] = [];

  public form = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    /** Le mot de passe est optionnel — null si l'utilisateur ne souhaite pas le modifier. */
    password: [''] 
  });

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private sessionService: SessionService,
    private topicService: TopicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUser();
    this.loadSubscriptions();
  }

  public loadUser(): void {
    this.userService.getMe().subscribe({
      next: (user: User) => {
        this.user = user;
        /** Pré-remplit le formulaire avec les données de l'utilisateur */
        this.form.patchValue({
          username: user.username,
          email: user.email,
          password: ''
        });
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  public loadSubscriptions(): void {
    this.topicService.getMySubscriptions().subscribe({
      next: (topics: Topic[]) => {
        this.subscriptions = topics;
      },
        error: (error) => {
        console.error('Erreur du chargement des abonnements :', error);
        }
    });
  }

  public unsubscribe(topic: Topic): void {
    this.topicService.unsubscribe(topic.id).subscribe({
      next: () => {
        /** Supprime le thème de la liste locale sans recharger la page */
        this.subscriptions = this.subscriptions.filter(s => s.id !== topic.id);
      },
      error: (error) => {
        console.error('Erreur lors du désabonnement :', error);
      }
    });
  }

  public submit(): void {
    if (this.form.invalid) return;

    const request: any = {
      username: this.form.value.username,
      email: this.form.value.email,
    };

    /** N'envoie le mot de passe que si l'utilisateur l'a modifié */
    if (this.form.value.password) {
      request.password = this.form.value.password;
    }

    this.userService.updateMe(request).subscribe({
      next: () => {
        this.onSuccess = true;
        this.onError = false;
      },
      error: () => {
        this.onError = true;
        this.onSuccess = false;
      }
    });
  }

  public logOut(): void {
    this.sessionService.logOut();
    this.router.navigate(['/login']);
  }
}