import { Component, ElementRef, ViewChild } from '@angular/core';
import { AuthUserService } from '../auth-user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-signup-login',
  templateUrl: './signup-login.component.html',
  styleUrls: ['./signup-login.component.css']
})
export class SignupLoginComponent {

  signUpObj: any = {
    name: '',
    username: '',
    password: '',
    isAdmin: false
  };
  loginObj: any = {
    username: '',
    password: ''
  };
  @ViewChild('loginLabel')
  loginLabel!: ElementRef;

  constructor(private authUserService: AuthUserService, private _router: Router) { }

  onSignUp(signUpForm: NgForm) {
    if (signUpForm.valid) {
      this.authUserService.signUpUser(this.signUpObj).subscribe((signUpResp: any) => {
        if (signUpResp.status) {
          this.signUpObj = {
            name: '',
            username: '',
            password: '',
            isAdmin: true
          };
          signUpForm.resetForm();
          this.loginLabel.nativeElement.click();
          alert(signUpResp.message);
        } else {
          alert(signUpResp.message);
        }
      });
    }
  }

  onLogin(loginForm: NgForm) {
    if (loginForm.valid) {
      this.authUserService.loginUser(this.loginObj).subscribe((loginResp:any) => {
        if (loginResp.status) {
        localStorage.setItem('loginAccessToken', loginResp.data.accessToken);
        this.authUserService.triggerFeedback();
          this._router.navigate(['feedBack']);
          this.loginObj = {
            username: '',
            password: ''
          };
          loginForm.resetForm();
          alert(loginResp.message);
        } else{
          alert(loginResp.message);
        }
      });
    }
  }
}
