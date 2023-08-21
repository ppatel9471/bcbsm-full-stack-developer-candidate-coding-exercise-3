import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import axios from 'axios';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthUserService {

  private api = "http://localhost:8093";
  private signUp = this.api + "/signup";
  private login = this.api + "/api/user/login";
  private getFeedbackData = this.api + "/api/user/getFeedback";
  private submitFeedbackData = this.api + "/api/user/submitFeedback";
  private logout = this.api + "/api/user/logout";
  private triggerFeedbackSource = new BehaviorSubject<boolean>(false);
  triggerFeedback$ = this.triggerFeedbackSource.asObservable();

  constructor(private http: HttpClient, private router: Router) { }
  triggerFeedback() {
    this.triggerFeedbackSource.next(true);
  }

  signUpUser(userData: any) {
    return this.http.post(this.signUp, userData);
  }

  loginUser(userData: any) {
    return this.http.post(this.login, userData);
  }

  getFeedback(accessToken: any) {
  return this.http.get(this.getFeedbackData,
    { 'headers': {
      'Authorization': `Bearer ${accessToken}`
    } })
  }

  submitFeedback(feedBackData: any,accessToken: any) :Observable<any>{
    const headerDict = { 'Authorization': 'Bearer ' + accessToken}
    const requestOptions = { headers: new HttpHeaders(headerDict) };
    return this.http.post(this.submitFeedbackData, feedBackData,requestOptions);
  }

  logoutUser(accessToken: any) {
    return this.http.get(this.logout,
      { 'headers': {
        'Authorization': `Bearer ${accessToken}`
      } })
    }
}
