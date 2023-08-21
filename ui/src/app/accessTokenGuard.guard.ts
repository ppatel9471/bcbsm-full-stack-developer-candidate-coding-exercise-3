import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AccessTokenGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    const accessToken = localStorage.getItem('loginAccessToken');
    if (accessToken) {
      return true;
    } else {
      return this.router.createUrlTree(['']); // Redirect to the default path (SignupLoginComponent)
    }
  }
}
