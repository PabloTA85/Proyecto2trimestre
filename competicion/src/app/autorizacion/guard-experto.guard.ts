import { Injectable } from '@angular/core';
import { LoginService } from '../servicios/login.service';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class guardExpertoGuard implements CanActivate {

  constructor(private loginService: LoginService, private router: Router) { }

  canActivate(): boolean {
    if (this.loginService.isExperto()) {
      return true;  // Si el usuario tiene el perfil de experto, permitir el acceso
    } else {
      this.router.navigate(['/login']); // Redirigir a login si no es experto
      return false;
    }
  }
}
