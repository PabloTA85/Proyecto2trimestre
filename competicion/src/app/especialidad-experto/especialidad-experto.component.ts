import { Component, OnInit } from '@angular/core';
import { EspecialidadService } from '../servicios/especialidad.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-especialidad-experto',
  imports: [CommonModule], // Sólo necesitas CommonModule para la funcionalidad de listar
  templateUrl: './especialidad-experto.component.html', // El archivo HTML que muestra las especialidades
  styleUrls: ['./especialidad-experto.component.css'] // El archivo CSS para los estilos de la vista
})

export class EspecialidadExpertoComponent implements OnInit {
  especialidades: any[] = []; // Array que almacenará las especialidades

  constructor(private especialidadService: EspecialidadService) { }

  ngOnInit(): void {
    this.obtenerEspecialidades(); // Llamamos al método obtenerEspecialidades() al inicializar el componente
  }

  obtenerEspecialidades(): void {
    this.especialidadService.getEspecialidades() // Llamamos al servicio para obtener las especialidades
      .subscribe(
        (data) => {
          this.especialidades = data; // Asignamos los datos recibidos a la variable especialidades
        },
        (error) => {
          console.error('Error al obtener las especialidades', error); // Manejo de errores si falla la obtención
        }
      );
  }
}
