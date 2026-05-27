import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathService = 'api/user';

  constructor(private httpClient: HttpClient) { }

  public getMe(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/me`);
  }

  public updateMe(request: { username?: string; email?: string; password?: string }): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/me`, request);
  }
}
