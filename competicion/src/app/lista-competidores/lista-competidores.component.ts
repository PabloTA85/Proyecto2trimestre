import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-lista-competidores',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-competidores.component.html',
  styleUrls: ['./lista-competidores.component.css'] // corregí styleUrl a styleUrls
})

export class ListaCompetidoresComponent implements OnInit {
  listaCompetidores: any[] = [];
  competidoresPaginados: any[] = [];
  paginaActual: number = 1;
  filasPorPagina: number = 5;
  totalPaginas: number = 1;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/api/participante/all')
      .subscribe(
        data => {
          // Mapeamos todos los campos que necesitamos mostrar en el front
          this.listaCompetidores = data.map(participante => ({
            nombre: participante.nombre,
            apellidos: participante.apellidos,
            email: participante.email,
            centro: participante.centro,
            especialidad: participante.nombreEspecialidad // Renombramos para simplificar la plantilla
          }));
          this.actualizarPaginacion(); // Calculamos la paginación al cargar los datos
        },
        error => {
          console.error('Error al obtener los participantes', error);
        }
      );
  }

  // Función para calcular la paginación
  actualizarPaginacion(): void {
    this.totalPaginas = Math.ceil(this.listaCompetidores.length / this.filasPorPagina);
    this.cargarPagina();
  }

  // Obtiene los datos de la página actual
  cargarPagina(): void {
    const inicio = (this.paginaActual - 1) * this.filasPorPagina;
    const fin = inicio + this.filasPorPagina;
    this.competidoresPaginados = this.listaCompetidores.slice(inicio, fin);
  }

  // Cambia de página
  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.cargarPagina();
    }
  }
}
