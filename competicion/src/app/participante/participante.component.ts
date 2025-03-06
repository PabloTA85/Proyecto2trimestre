import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OnInit } from '@angular/core';
import * as bootstrap from 'bootstrap';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../servicios/login.service';

interface Especialidad {
  idEspecialidad: number;
  nombre: string;
}

@Component({
  selector: 'app-participante',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './participante.component.html',
  styleUrls: ['./participante.component.css']

})

export class ParticipanteComponent implements OnInit {
  especialidades: any;
  token: string = '';
  eliminarCompetidor(_t17: any) {
    throw new Error('Method not implemented.');
  }

  listaParticipantes: any[] = [];
  participantesPaginados: any[] = [];
  participante: any = {};
  paginaActual: number = 1;
  filasPorPagina: number = 8;
  totalPaginas: number = 1;
  mostrarModal: boolean = false;
  competidoresPaginados: any;

  constructor(private http: HttpClient, private loginService: LoginService) { }

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/api/participante/all')
      .subscribe(
        data => {
          console.log('Participantes recibidos:', data);
  
          // Mapeamos todos los campos que necesitamos mostrar en el front
          this.listaParticipantes = data.map(participante => ({
            ...participante,  // Copia todos los campos originales
            especialidad: participante.nombreEspecialidad, // Renombramos para simplificar la plantilla
            id: participante.idParticipante // Asignamos el id correctamente
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
    this.totalPaginas = Math.ceil(this.listaParticipantes.length / this.filasPorPagina);
    this.cargarPagina();
  }

  // Obtiene los datos de la página actual
  cargarPagina(): void {
    const inicio = (this.paginaActual - 1) * this.filasPorPagina;
    const fin = inicio + this.filasPorPagina;
    this.participantesPaginados = this.listaParticipantes.slice(inicio, fin);
  }

  // Cambia de página
  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.cargarPagina();
    }
  }

  abrirModal(participante: any) {
    console.log("Participante seleccionado:", participante);
    this.participante = { ...participante };  // Copiar todos los campos del participante
  
    // Verifica si el id está presente en el participante
    console.log("ID del participante:", this.participante.id);
  
    // Asegurarnos de que la especialidad esté correctamente asignada
    this.cargarEspecialidad(participante);
  
    // Mostrar el modal
    this.mostrarModal = true;
  
    setTimeout(() => {
      const modalElement = document.getElementById('modalModificar') as HTMLElement;
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }, 1);
  }
  



  // Cargar la especialidad seleccionada si es necesario
  cargarEspecialidad(participante: any) {
    // Si el participante tiene un idEspecialidad, lo asignamos
    if (participante.idEspecialidad) {
      this.participante.idEspecialidad = participante.idEspecialidad;
    }
  }


  cerrarModal() {
    this.mostrarModal = false;
    const modalElement = document.getElementById('modalModificar');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.hide();  // Cerrar el modal
    }
  }

  obtenerEspecialidades(): void {
    const headers = new HttpHeaders({ 'Authorization': 'Bearer ' + this.token });
    this.http.get<Especialidad[]>('http://localhost:8080/api/especialidad/all', { headers })
      .subscribe({
        next: (data) => {
          console.log("Especialidades recibidas:", data);
          this.especialidades = data;

          // Después de obtener las especialidades, asigna el idEspecialidad al participante si ya está definido
          if (this.participante.nombreEspecialidad) {
            this.asignarIdEspecialidad();  // Llamar a la asignación cuando las especialidades estén disponibles
          }
        },
        error: (error) => {
          console.error('Error al obtener las especialidades', error);
        }
      });
  }

  // Asignar idEspecialidad cuando las especialidades están cargadas
  asignarIdEspecialidad(): void {
    if (this.participante.nombreEspecialidad) {
      const especialidad = this.especialidades.find((e: Especialidad) => e.nombre === this.participante.nombreEspecialidad);
      if (especialidad) {
        this.participante.idEspecialidad = especialidad.idEspecialidad;
        console.log("ID Especialidad asignado:", this.participante.idEspecialidad);
      }
    }
  }


  modificarParticipante(): void {
    if (!this.participante.id) {
      alert("ID del participante no válido.");
      return;
    }
  
    if (!this.participante.nombreEspecialidad) {
      alert("Seleccione una especialidad válida");
      return;
    }
  
    const participanteModificado = {
      nombre: this.participante.nombre,
      apellidos: this.participante.apellidos,
      email: this.participante.email,
      centro: this.participante.centro,
      Especialidad: this.participante.nombreEspecialidad
    };
  
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.token,
      'Content-Type': 'application/json'
    });
  
    this.http.put(`http://localhost:8080/api/participante/${this.participante.id}`, participanteModificado, { headers })
      .subscribe({
        next: (data) => {
          console.log("Participante modificado:", data);
          this.cerrarModal();  // Cerrar el modal después de la modificación
          this.ngOnInit();  // Recargar los datos
        },
        error: (error) => {
          console.error('Error al modificar el participante', error);
          alert("Hubo un error al modificar el participante");
        }
      });
  }
  

}



