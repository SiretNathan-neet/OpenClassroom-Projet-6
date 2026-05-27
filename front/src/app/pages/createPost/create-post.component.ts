import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostService } from 'src/app/services/post.service';
import { TopicService } from 'src/app/services/topic.service';
import { Topic } from 'src/app/interfaces/topic.interface';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {

  public topics: Topic[] = [];
  public onError = false;

  public form = this.fb.group({
    topicId: ['', [Validators.required]],
    title: ['', [Validators.required]],
    content: ['', [Validators.required]]
  });

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private topicService: TopicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.topicService.getAllTopics().subscribe({
      next: (topics: Topic[]) => {
        this.topics = topics;
      },
      error: (error) => {
        console.error('Erreur chargement thèmes :', error);
      }
    });
  }

  public submit(): void {
    if (this.form.invalid) return;

    const request = {
      /** topicId est stocké comme string dans le formulaire — conversion explicite en number */ 
      topicId: Number(this.form.value.topicId),
      title: this.form.value.title as string,
      content: this.form.value.content as string
    };

    this.postService.createPost(request).subscribe({
      next: () => {
        this.router.navigate(['/feed']);
      },
      error: () => {
        this.onError = true;
      }
    });
  }
}