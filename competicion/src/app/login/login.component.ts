import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../servicios/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  usuario: string;
  contrasena: string;
  errorMessage: string = "";
  constructor(private loginService: LoginService, private route: Router) {
    this.usuario = '';
    this.contrasena = '';
  }

  logear(): void {
    this.loginService.login(this.usuario, this.contrasena).subscribe(
      (v) => {
        console.log(v); // Agregado para ver la respuesta completa
        if (v.funciona) {
          if (v.perfil == "ROLE_ADMIN") {
            this.route.navigate(['/admin']);
          } else {
            this.route.navigate(['experto']);
          }
        }
      },
      (error) => {
        if (error.status == 401) {
          this.errorMessage = "Usuario o contraseña incorrectos";
        } else {
          this.errorMessage = "Error en la autenticación";
        }
      }
    ); // Aquí cerramos correctamente el subscribe
  }

  
  // Verifica si el usuario está logeado
  get isLogged(): boolean {
    return this.loginService.isLogged();
  }
  
}

