import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LogoComponent } from "./logo/logo.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from './navbar/navbar.component';
import { ListaCompetidoresComponent } from "./lista-competidores/lista-competidores.component";
import { FooterComponent } from "./footer/footer.component";
import { ExpertoComponent } from './experto/experto.component';
import { ParticipanteComponent } from './participante/participante.component';
import { NotasComponent } from './notas/notas.component';

@Component({
  selector: 'app-root',
  standalone: true, // Componente independiente
  imports: [
    CommonModule,
    FormsModule,
    RouterOutlet,
    LogoComponent,
    NavbarComponent,
    ListaCompetidoresComponent,
    FooterComponent,
    ExpertoComponent,
    ParticipanteComponent,
    NotasComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] // <-- Corregido
})
export class AppComponent {
  title = 'competicion';
  nombre: string = 'MI WEB';

  constructor() { }
}
