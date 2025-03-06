import { Injectable } from '@angular/core';
import { LoginService } from '../servicios/login.service';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class guardAdminGuard implements CanActivate {

  constructor(private loginService: LoginService, private router: Router) { }

  canActivate(): boolean {
    if (this.loginService.isAdmin()) {
      return true;
    } else {
      // Redirige al login o a otra página según tus necesidades
      this.router.navigate(['/login']);
      return false;
    }
  }
}
