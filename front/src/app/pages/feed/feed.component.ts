import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from 'src/app/interfaces/post.interface';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {

  public posts: Post[] = [];
  public sort: string = 'desc';
  public isEmpty: boolean = false;

  constructor(
    private postService: PostService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFeed();
  }

  public loadFeed(): void {
    this.postService.getFeed(this.sort).subscribe({
      next: (posts: Post[]) => {
        this.posts = posts;
        this.isEmpty = posts.length === 0;
      },
      error: (error) => {
        console.error('Erreur chargement de la page feed :', error);
      }
    });
  }
  
  /** Inverse le tri et recharge le feed. */
  public toggleSort(): void {
    this.sort = this.sort === 'desc' ? 'asc' : 'desc';
    this.loadFeed();
  }

  public goToPost(id: number): void {
    this.router.navigate(['/posts', id]);
  }

  public goToCreatePost(): void {
    this.router.navigate(['/posts/create']);
  }
}