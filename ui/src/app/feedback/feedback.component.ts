import { Component, OnInit } from '@angular/core';
import { AuthUserService } from '../auth-user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit{

  isFeedbackAvailable:any=[];
  isAdmin:any;
  allFeedBack:any;

  feedBackObj: any = {
    rating_value: 0,
    comment: ''
  };

  constructor(private authUserService: AuthUserService, private _router: Router){}

  ngOnInit(){
    this.authUserService.triggerFeedback$.subscribe((trigger) => {
      if(trigger){
        this.getFeedback();
      }
    })
  }

  getFeedback(){
    this.authUserService.getFeedback(localStorage.getItem('loginAccessToken')).subscribe((feedbackRes:any) => {
      this.allFeedBack = feedbackRes.data
      this.isFeedbackAvailable = feedbackRes.data.length > 0 ? true: false
    });
  }

  onSubmitFeedBack(feedbackForm: NgForm) {
    if (feedbackForm.valid) {
      this.authUserService.submitFeedback(this.feedBackObj,localStorage.getItem('loginAccessToken')).subscribe((feedBackResp: any) => {
        if (feedBackResp.status) {
          this.feedBackObj = {
            rating_value: '',
            comment: ''
          };
          feedbackForm.resetForm();
          this.allFeedBack.push(feedBackResp.data)
          this.isFeedbackAvailable = this.allFeedBack.length > 0 ? true: false;
        } else {
          alert(feedBackResp.message);
        }
      });
    }
  }

  logoutUser(){
    this.authUserService.logoutUser(localStorage.getItem('loginAccessToken')).subscribe((logoutRes:any) => {
      if(logoutRes.status){
        localStorage.removeItem('loginAccessToken');
        this._router.navigate(['']);
        alert(logoutRes.message);
      }
    });
  }

}
