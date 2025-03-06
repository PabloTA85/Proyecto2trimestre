import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../servicios/login.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registro',
  standalone: true,  // Especifica que este componente es independiente
  imports: [CommonModule, FormsModule],  // Agrega FormsModule aquí
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})

export class RegistroComponent {
  usuario: string;
  correo: string;
  contrasena: string;
  confirmarContrasena: string;

  constructor(private loginServicio: LoginService, private route: Router) {
    this.usuario = '';
    this.correo = '';
    this.contrasena = '';
    this.confirmarContrasena = '';
  }

  registrar(): void {
    if (this.contrasena !== this.confirmarContrasena) {
      alert("Las contraseñas no coinciden");
      return;
    }

    this.loginServicio.registrar(this.usuario, this.contrasena, this.correo).subscribe((response) => {
      if (response.funciona) {
        alert("Registro exitoso, puedes iniciar sesión ahora.");
        this.route.navigate(['/login']); // Redirige al login
      } else {
        alert("Error en el registro. Intenta nuevamente.");
      }
    });
  }
}
