import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { User } from "../models/user.model";

@Injectable(
    {
      providedIn: 'root'
    }
)
export class UsersService {
    private apiURL = 'http://localhost:8080/api/users';

    constructor(private http: HttpClient) {}
    
    getAllUsers(): Observable<User[]> {
        return this.http.get<User[]> (`${this.apiURL}`);
    }

    getUserByUsername(username: string): Observable<User> {
        return this.http.post<User>(`${this.apiURL}`, { username });
    }

     createUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiURL}`, user);
  }

}