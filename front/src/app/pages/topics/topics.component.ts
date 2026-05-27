import { Component, OnInit } from '@angular/core';
import { TopicService } from 'src/app/services/topic.service';
import { Topic } from 'src/app/interfaces/topic.interface';

@Component({
  selector: 'app-topics',
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.scss']
})
export class TopicsComponent implements OnInit {

  public topics: Topic[] = [];

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.loadTopics();
  }

  public loadTopics(): void {
    this.topicService.getAllTopics().subscribe({
      next: (topics: Topic[]) => {
        this.topics = topics;
      },
      error: (error) => {
        console.error('Erreur chargement thèmes :', error);
      }
    });
  }

  public subscribe(topic: Topic): void {
    this.topicService.subscribe(topic.id).subscribe({
      next: () => {
        /** Met à jour l'état de l'abonnement du thème localement*/
        topic.subscribed = true;
      },
      error: (error) => {
        console.error('Erreur abonnement :', error);
      }
    });
  }
}