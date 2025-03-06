import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginService } from '../servicios/login.service';
import { FormsModule } from '@angular/forms';
import * as bootstrap from 'bootstrap';
import { EvaluacionService } from '../servicios/evaluacion.service';

@Component({
  selector: 'app-prueba',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './evaluacion.component.html',
  styleUrls: ['./evaluacion.component.css']
})
export class EvaluacionComponent implements OnInit {

  pruebas: any[] = [];  // Array para almacenar todas las pruebas
  errorMessage: string = '';  // Propiedad para errores
  usuario: string = '';  // Propiedad para almacenar el nombre de usuario
  listaParticipantes: any[] = [];  // Array para almacenar los participantes
  participanteSeleccionado: any = null;
  pruebaSeleccionada: any = null;
  token: string = '';  // Guardar el token
  especialidades: any[] = [];  // Guardar especialidades
  collapsedStates: { [id: string]: boolean } = {};  // Guardar el estado de colapso para cada prueba
  nuevaPrueba: any;  // Variable para nueva prueba
  notaFinal: number = 0;  // Variable para la nota final
  modoEvaluacion: any;
  evaluando: any;
  evaluacionSeleccionada: any;
  evaluacionId: number = 0;

  // Función para alternar el estado de colapso
  toggleCollapse(id: string): void {
    this.collapsedStates[id] = !this.collapsedStates[id];
  }

  // Función para comprobar si una prueba está colapsada
  isCollapsed(id: string): boolean {
    return this.collapsedStates[id] === true;
  }

  

  constructor(
    @Inject(EvaluacionService) private evaluacionService: EvaluacionService,
    private http: HttpClient,
    private loginService: LoginService
  ) { }

  ngOnInit(): void {
    this.iniciarSesion();
  }

  iniciarSesion(): void {
    this.token = this.loginService.getToken();
    this.usuario = this.loginService.getNombre();

    if (this.token && this.usuario) {
      this.getPruebas();  // Llamar a getPruebas si el token y usuario están presentes
    } else {
      this.errorMessage = 'No se ha encontrado un token de autenticación o el usuario no está definido. Por favor, inicie sesión.';
    }
  }

  ngAfterViewInit(): void {
    let modalElement = document.getElementById('evaluarParticipanteModal');

    if (modalElement) {
      modalElement.addEventListener('hidden.bs.modal', () => {
        this.resetEvaluacion();
      });
    }
  }

  resetEvaluacion(): void {
    console.log("Reseteando evaluación...");
    this.evaluando = false;
    this.notaFinal = 0;
  }

  getPruebas(): void {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.token}`)
      .set('Content-Type', 'application/json');

    this.http.get<any[]>(`http://localhost:8080/api/pruebas/porEspecialidad/${this.usuario}`, { headers })
      .subscribe({
        next: (data: any[]) => {
          this.pruebas = data;
        },
        error: (err: any) => {
          console.error('Error al obtener las pruebas:', err);
          this.errorMessage = 'Hubo un error al cargar las pruebas';
        }
      });
  }

  // Función para obtener los participantes de la prueba seleccionada
  ObtenerParticipantes(): void {
    if (!this.usuario) {
      this.errorMessage = 'El usuario no está definido.';
      return;
    }

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.token}`)
      .set('Content-Type', 'application/json');

    // Solo obtener los participantes de la especialidad de la prueba seleccionada
    this.http.get<any[]>(`http://localhost:8080/api/participante/porEspecialidad/${this.usuario}`, { headers })
      .subscribe({
        next: (data: any[]) => {
          this.listaParticipantes = data.filter(participante => participante.pruebaId === this.pruebaSeleccionada.id);
        },
        error: (err: any) => {
          console.error('Error al obtener los participantes', err);
          this.errorMessage = 'Hubo un error al cargar los participantes';
        }
      });
  }

  seleccionarParticipante(participante: any, prueba: any): void {
    // Establecer los participantes y pruebas seleccionados
    this.participanteSeleccionado = participante;
    this.pruebaSeleccionada = prueba;

    // Asegurarse de que todos los grados seleccionados sean 0 si no están definidos
    this.pruebaSeleccionada.items.forEach((item: any) => {
      if (item.gradoSeleccionado === undefined) {
        item.gradoSeleccionado = 0;
      }
    });

    // Abrir el modal
    const modal = new bootstrap.Modal(document.getElementById('evaluarParticipanteModal')!);
    modal.show();

    // LLamar a la función para guardar la evaluación después de abrir el modal
    //this.guardarEvaluacion();
  }
  

  seleccionarPrueba(prueba: any): void {
    // Establecer la prueba seleccionada
    this.pruebaSeleccionada = prueba;

    // Obtener los participantes solo de la prueba seleccionada
    this.ObtenerParticipantes();
  }


  generarRango(max: number): number[] {
    return Array.from({ length: max + 1 }, (_, i) => i);
  }

  guardarEvaluacion(): void {
    if (!this.participanteSeleccionado || !this.pruebaSeleccionada) {
      this.errorMessage = 'Debe seleccionar un participante y una prueba.';
      return;
    }
    const evaluacionDTO = {
      participanteId: this.participanteSeleccionado.idParticipante,
      evaluadorId: this.loginService.getIdUser(),
      pruebaId: this.pruebaSeleccionada.idPrueba,
      notaFinal: 0.0
    }
    this.evaluacionService.crearEvaluacion(evaluacionDTO).subscribe({
      next: (response: any) => {
        this.evaluacionId = response.idEvaluacion;
        this.evaluarItems();
        // Cerrar el modal después de guardar
      },
      error: (err: any) => {
        console.error('Error al guardar la evaluación', err);
        this.errorMessage = 'Hubo un error al guardar la evaluación';
      }
    });
  }

  evaluarItems() {
    const evaluacion = {
      evaluacionId: this.evaluacionId, // ID de la evaluación
      participanteId: this.participanteSeleccionado.idParticipante, // ID del participante
      pruebaId: this.pruebaSeleccionada.idPrueba, // ID de la prueba
      items: this.pruebaSeleccionada.items.map((item: any) => ({
        itemId: item.idItem,
        valoracion: item.gradoSeleccionado || 0, // Grado seleccionado o 0 por defecto
        comentario: item.comentario || '', // Comentario del ítem
      })),
    };
    this.evaluacionService.evaluarItems(evaluacion).subscribe({
      next: (response: any) => {
        console.log('Evaluación guardada con éxito', response);
        this.notaFinal = response.total;  // Reiniciar la nota final
        this.participanteSeleccionado = null;
        this.pruebaSeleccionada = null;
        this.evaluando = false;
        //this.cerrarModal();  // Cerrar el modal después de guardar
      },
      error: (err: any) => {
        console.error('Error al guardar la evaluación', err);
        this.errorMessage = 'Hubo un error al guardar la evaluación';
      }
    });
  }

  // Método para cerrar el modal
  cerrarModal(): void {
    const modalElement = document.getElementById('evaluarParticipanteModal');
    if (modalElement) {
      const modalInstance = bootstrap.Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    }
  }
}
