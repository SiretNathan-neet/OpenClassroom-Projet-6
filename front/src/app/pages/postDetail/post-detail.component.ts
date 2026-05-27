import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { PostService } from 'src/app/services/post.service';
import { PostDetail, PostComment } from 'src/app/interfaces/post.interface';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit {

  public post: PostDetail | undefined;

  public commentForm = this.fb.group({
    content: ['', [Validators.required]]
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    /** Récupère l'ID du post à partir de l'URL */
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getPostById(id).subscribe({
      next: (post: PostDetail) => {
        this.post = post;
      },
      error: () => {
        this.router.navigate(['/feed']);
      }
    });
  }

  public submitComment(): void {
    if (this.commentForm.invalid || !this.post) return;

    const content = this.commentForm.value.content;
    if (!content) return;

    this.postService.addComment(this.post.id, { content }).subscribe({
      next: (comment: PostComment) => {
        /** Ajoute le commentaire à la liste locale sans recharger la page */
        this.post!.comments.push(comment);
        this.commentForm.reset();
      },
      error: (error) => {
        console.error('Erreur ajout commentaire :', error);
      }
    });
  }
}