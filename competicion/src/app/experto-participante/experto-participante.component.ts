import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import * as bootstrap from 'bootstrap';
import { LoginService } from '../servicios/login.service';

@Component({
  selector: 'app-participante',
  imports: [FormsModule, CommonModule],
  templateUrl: './experto-participante.component.html',
  styleUrls: ['./experto-participante.component.css']
})
export class ExpertoParticipanteComponent implements OnInit {


  listaParticipantes: any[] = [];
  participantesPaginados: any[] = [];
  paginaActual: number = 1;
  filasPorPagina: number = 7;
  totalPaginas: number = 1;
  token: string = ''; // Add this line to define the token property
  especialidades: any[] = [];
  participante: any = {}; // Almacenará el participante que estamos modificando
  usuario: string = '';

  constructor(private http: HttpClient, private loginService: LoginService
  ) {
    this.usuario = this.loginService.getNombre();
  }

  ngOnInit(): void {
    this.token = this.loginService.getToken();
    this.ObtenerParticipantes();
  }




  agregarParticipante() {
    // Verificar que los datos básicos están completos
    if (!this.participante.nombre || !this.participante.apellidos || !this.participante.email || !this.participante.centro) {
      alert("Por favor, completa todos los campos.");
      return;
    }

    // Obtener el idUser desde el servicio de login
    const idUser = this.loginService.getIdUser();
    if (!idUser) {
      alert("No se pudo obtener el id del usuario.");
      return;
    }

    // Crear el objeto con los datos del participante
    const participanteData = {
      nombre: this.participante.nombre,
      apellidos: this.participante.apellidos,
      email: this.participante.email,
      centro: this.participante.centro
    };

    // Definir los headers de la solicitud con el token
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.loginService.getToken(),
      'Content-Type': 'application/json'
    });

    // Realizar la solicitud HTTP POST
    const url = `http://localhost:8080/api/participante/create/byEspecialidad/${idUser}`;

    this.http.post(url, participanteData, { headers })
      .subscribe({
        next: (response: any) => {
          // Verificar la respuesta en la consola
          console.log("Respuesta del servidor:", response);

          // Comprobar que la respuesta es válida y tiene el status 'success'
          if (response && response.status === 'success') {
            alert("Participante agregado con éxito.");
            this.participante = {}; // Limpiar formulario
            this.ObtenerParticipantes(); // Refrescar la lista de participantes
          } else {
            console.error("Error en la respuesta:", response.message);
          }
        },
        error: (error) => {
          console.error('Error inesperado:', error);
        }
      });
  }




  eliminarParticipante(participante: any): void {
    const headers = new HttpHeaders()
      .set('Authorization', 'Bearer ' + this.token) // Si necesitas un token para autenticar la solicitud
      .set('Content-Type', 'application/json');

    // Realizar la solicitud DELETE al backend para eliminar al participante
    this.http.delete(`http://localhost:8080/api/participante/${participante.idParticipante}`, { headers })
      .subscribe({
        next: (response: any) => {
          console.log('Participante eliminado', response);
          alert('Participante eliminado con éxito');
          this.ngOnInit(); // Refrescar la lista de participantes si es necesario
          this.cerrarModal();
        },
        error: (error) => {
          console.error('Error al eliminar participante', error);
          alert('Hubo un error al eliminar al participante');
        }
      });
  }



  ObtenerParticipantes(): void {
    console.log("Token antes de la petición:", this.token);

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.token}`)
      .set('Content-Type', 'application/json');

    console.log("Headers de la petición:", headers);

    this.http.get<any[]>('http://localhost:8080/api/participante/porEspecialidad/' + this.usuario, { headers })
      .subscribe({
        next: (data: any[]) => {
          this.listaParticipantes = data;
          this.actualizarPaginacion();
        },
        error: (err) => {
          console.error('Error al obtener los participantes', err);
        }
      });
  }


  actualizarPaginacion(): void {
    this.totalPaginas = Math.ceil(this.listaParticipantes.length / this.filasPorPagina);
    this.cargarPagina();
  }

  cargarPagina(): void {
    const inicio = (this.paginaActual - 1) * this.filasPorPagina;
    const fin = inicio + this.filasPorPagina;
    this.participantesPaginados = this.listaParticipantes.slice(inicio, fin);
  }

  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.cargarPagina();
    }
  }

  abrirModalModificar(participante: any): void {
    this.participante = { ...participante };
    console.log('Participante a modificar:', this.participante);  // Asegúrate de que el ID esté aquí
  }


  modificarParticipante(): void {
    console.log('ID del participante:', this.participante.idParticipante);
    if (!this.participante.idParticipante) {
      alert("ID del participante no válido.");
      return;
    }

    console.log('Datos del participante:', this.participante.nombre, this.participante.apellidos, this.participante.email, this.participante.centro);
    if (!this.participante.nombre || !this.participante.apellidos || !this.participante.email || !this.participante.centro) {
      alert("Por favor, complete todos los campos.");
      return;
    }

    const participanteModificado = {
      nombre: this.participante.nombre,
      apellidos: this.participante.apellidos,
      email: this.participante.email,
      centro: this.participante.centro
    };

    const headers = new HttpHeaders()
      .set('Authorization', 'Bearer ' + this.token)
      .set('Content-Type', 'application/json');

    console.log('Encabezados antes de la solicitud:', headers);

    this.http.put(`http://localhost:8080/api/participante/${this.participante.idParticipante}`, participanteModificado, { headers })
      .subscribe({
        next: (response: any) => {
          if (response.status === 'success') {
            alert(response.message); // Mostrar el mensaje de éxito
            this.cerrarModal();
            this.ngOnInit();
          } else {
            console.error('Error en la respuesta:', response.message);
          }
        },
        error: (error) => {
          console.error('Error al modificar el participante', error);
          alert("Hubo un error al modificar el participante");
          console.log('Detalles del error:', error);
          if (error.status === 200) {
            console.log('Respuesta exitosa pero con mensaje incorrecto:', error);
          }
        }
      });
  }


  cerrarModal(): void {
    const modal = document.getElementById('modificarModal');
    if (modal) {
      const modalInstance = bootstrap.Modal.getInstance(modal);
      if (modalInstance) {
        modalInstance.hide(); // Cierra el modal
      }
    }
  }
}
