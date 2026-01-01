import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UsersService } from './services/users.service';
import {User} from '../app/models/user.model'


@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  users: User[] = [];
  constructor (private usersService: UsersService) {
    this.loadUsers();
  }

  loadUsers()
  {
    this.usersService.getAllUsers().subscribe(
      {
       next: (data) => {
        this.users = data;
        console.log('Users loaded: ', this.users)
       }, 
       error: (err) => console.error('Error loading users: ', err)
      }
    );
  }
}



