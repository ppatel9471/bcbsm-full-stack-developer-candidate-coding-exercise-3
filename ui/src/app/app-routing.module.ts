import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupLoginComponent } from './signup-login/signup-login.component';
import { FeedbackComponent } from './feedback/feedback.component';
import { AccessTokenGuard } from './accessTokenGuard.guard';

const routes: Routes = [
  {
    path: "",
    component: SignupLoginComponent
  },
  {
    path: "feedBack",
    component: FeedbackComponent,
    canActivate: [AccessTokenGuard]
  },
  {
    path: "**", // Wildcard route for undefined paths
    redirectTo: "" // Redirect to the default path
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
