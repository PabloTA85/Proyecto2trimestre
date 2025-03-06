import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../servicios/login.service';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {


  mostrarPuntuacionesExperto: any;
  mostrarParticipantesExperto() {
    this.router.navigate(['/experto/experto-participante']);
  }

  mostrarEspecialidadesExperto() {
    this.router.navigate(['/experto/especialidad-experto']);
  }


  mostrarPruebas() {
    this.router.navigate(['/experto/prueba']);
  }

  mostrarEvaluacion() {
    this.router.navigate(['/experto/evaluacion']);
  }
  mostrarItems() {
    this.router.navigate(['/experto/item']);
  }

  perfil: string;
  nombre: string;

  constructor(private router: Router, public loginService: LoginService) { // Cambiar "servicio" por "loginService"
    this.nombre = '';
    this.perfil = '';
  }

  logout() {
    this.loginService.logout();
    this.nombre = '';
    this.router.navigate(['/']);
  }

  login() {
    this.router.navigate(['/login']);
  }

  islogged() {
    return this.loginService.isLogged();
  }

  getNombre() {
    return this.loginService.getNombre();
  }

  registrar() {
    this.router.navigate(['/registro']);
  }


  // Método que maneja el clic en el botón de experto
  mostrarExpertos(): void {
    this.router.navigate(['/admin/experto']);
  }

  // Método que maneja el clic en el botón de especialidad
  mostrarEspecialidad(): void {
    this.router.navigate(['/admin/especialidad']);
  }

  // Método que maneja el clic en el botón de participante
  mostrarParticipantes(): void {
    this.router.navigate(['/admin/participante']);
  }

  mostrarNotas() {
    this.router.navigate(['/experto/notas']);
    }

}
