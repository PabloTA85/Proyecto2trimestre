import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../servicios/login.service';
import * as bootstrap from 'bootstrap';

interface Experto {
  nombre: string;
  apellidos: string;
  username: string;
  password?: string;
  dni: string;
  role?: string;
  especialidad: string;
}

interface Especialidad {
  idEspecialidad: number;
  nombre: string;
}

@Component({
  selector: 'app-experto',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
  ],
  templateUrl: './experto.component.html',
  styleUrls: ['./experto.component.css']
})
export class ExpertoComponent implements OnInit {
  expertos: Experto[] = [];
  experto: Experto = {
    nombre: '',
    apellidos: '',
    username: '',
    password: '',
    dni: '',
    especialidad: '',
  };
  token: string = "";
  mostrarModal: boolean = false;
  especialidades: Especialidad[] = [];
  expertosPorPagina: Experto[] = [];
  paginaActual = 1;
  totalPaginas = 0;
  filasPorPagina = 5;

  constructor(private http: HttpClient, private loginService: LoginService) { }

  ngOnInit(): void {
    console.log('Cargando el componente Experto');
    this.token = this.loginService.getToken();
    console.log("Token:", this.token);
    this.obtenerExpertos();
    this.obtenerEspecialidades();
  }

  obtenerExpertos(): void {
    this.http.get<any[]>('http://localhost:8080/usuario/expertos', {
      headers: new HttpHeaders({ 'Authorization': 'Bearer ' + this.token })
    })
      .subscribe({
        next: (data) => {
          console.log('Datos recibidos de la API:', data);

          // Revisa aquí si idUser está presente
          data.forEach(experto => {
            console.log(experto.idUser);  // Debería imprimir el idUser
          });

          this.expertos = data.map(experto => ({
            idUser: experto.idUser,
            nombre: experto.nombre,
            apellidos: experto.apellidos,
            username: experto.username,
            dni: experto.dni,
            especialidad: experto.especialidad || { idEspecialidad: null, nombre: 'Sin especialidad' },
            password: experto.password || '',
            role: experto.role || ''
          }));

          this.actualizarPaginacion();  // Calculamos la paginación al cargar los datos
        },
        error: (error) => {
          console.error('Error al obtener los expertos', error);
        }
      });
  }


  // Función para calcular la paginación
  actualizarPaginacion(): void {
    this.totalPaginas = Math.ceil(this.expertos.length / this.filasPorPagina);
    this.cargarPagina();
  }

  // Obtiene los datos de la página actual
  cargarPagina(): void {
    const inicio = (this.paginaActual - 1) * this.filasPorPagina;
    const fin = inicio + this.filasPorPagina;
    this.expertosPorPagina = this.expertos.slice(inicio, fin);
  }

  // Cambia de página
  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.cargarPagina();
    }
  }

  obtenerEspecialidades(): void {
    const headers = new HttpHeaders({ 'Authorization': 'Bearer ' + this.token });
    this.http.get<Especialidad[]>('http://localhost:8080/api/especialidad/all', { headers })
      .subscribe({
        next: (data) => {
          console.log("Especialidades recibidas:", data);
          this.especialidades = data;
        },
        error: (error) => {
          console.error('Error al obtener las especialidades', error);
        }
      });
  }

  agregarExperto(): void {
    this.resetExperto();
  }

  // Abrir el modal y preparar los datos para la modificación
  abrirModal(experto: any) {
    this.experto = { ...experto };  // Usamos spread operator para copiar los datos del experto seleccionado
    this.mostrarModal = true;       // Mostrar el modal para editar el experto
    console.log(this.experto)
    // Usamos JavaScript para activar el modal de Bootstrap
    setTimeout(() => {
      const modalElement = document.getElementById('modalModificar') as HTMLElement;
      const modal = new bootstrap.Modal(modalElement);
      modal.show();  // Ahora creamos la instancia y llamamos a `show()` sobre ella
    }, 1);
  }

  // Cerrar el modal
  cerrarModal() {
    this.mostrarModal = false;
    const modalElement = document.getElementById('modalModificar');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.hide();  // Cerrar el modal
    }
  }

  crearExperto(): void {
    // Verificar si la especialidad está correctamente seleccionada
    if (!this.experto.especialidad) {

      return;
    }

    const especialidad: any = this.experto.especialidad;
    const expertoData = {
      nombre: this.experto.nombre,
      apellidos: this.experto.apellidos,
      username: this.experto.username,
      password: this.experto.password,
      dni: this.experto.dni,
      role: 'EXPERTO',
      idEspecialidad: especialidad.idEspecialidad // Asegúrate de que aquí esté el id
    };

    console.log("Enviando datos del experto:", expertoData);

    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.token,
      'Content-Type': 'application/json'
    });

    this.http.post('http://localhost:8080/api/auth/register', expertoData, { headers })
      .subscribe({
        next: (response: any) => {
          console.log('Respuesta del servidor:', response);
          alert(response.message || "Experto creado con éxito.");

          // Actualizar la lista de expertos
          this.obtenerExpertos();

          // Llamamos al cierre del modal de forma asíncrona para evitar el bloqueo
          this.cierraModal();

          // Resetear el formulario
          this.resetExperto();

          // Actualizar la paginación si es necesario
          this.actualizarPaginacion();
        },
        error: (error) => {
          console.error('Error al crear el experto', error);
          alert('Hubo un error al crear el experto.');
        }
      });
  }


  cierraModal(): void {
    const modalElement = document.getElementById('modalExperto');
    if (modalElement) {
      const modal = bootstrap.Modal.getInstance(modalElement); // Obtiene la instancia del modal
      if (modal) {
        setTimeout(() => {
          modal.hide(); // Cierra el modal después de un pequeño retraso
        }, 0);
      }
    }
  }




  modificarExperto(experto: any) {
    console.log("Especialidad seleccionada para la modificación:", experto.especialidad);

    // Validar si la especialidad es válida
    if (!experto.especialidad) {
      alert('Por favor seleccione una especialidad válida.');
      return;
    }

    // Buscar la especialidad en el array de especialidades
    const especialidad = this.especialidades.find(e => e.nombre === experto.especialidad);

    // Si no se encuentra la especialidad, mostrar un mensaje y terminar la función
    if (!especialidad) {
      alert('La especialidad seleccionada no es válida.');
      return;
    }

    // Crear el objeto con los datos modificados
    const expertoData = {
      idUser: experto.idUser,
      nombre: experto.nombre,
      apellidos: experto.apellidos,
      username: experto.username,
      dni: experto.dni,
      idEspecialidad: especialidad.idEspecialidad  // Usar la idEspecialidad encontrada
    };

    console.log("Enviando datos del experto para modificar:", expertoData);

    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.token,
      'Content-Type': 'application/json'
    });

    console.log('ID del experto:', experto.id || experto.idUser);  // Verifica que el id esté presente

    // Si el id es parte de un campo diferente, usa esa propiedad
    this.http.put(`http://localhost:8080/usuario/${experto.id || experto.idUser}`, expertoData, { headers })
      .subscribe({
        next: (response: any) => {
          console.log('Respuesta del servidor al modificar experto:', response);
          alert(response.message || "Experto modificado con éxito.");
          this.obtenerExpertos();  // Actualizar la lista de expertos
          this.cerrarModal();      // Cerrar el modal
        },
        error: (error) => {
          console.error('Error al modificar el experto', error);
          alert('Hubo un error al modificar el experto.');
        }
      });
  }




  resetExperto() {
    this.experto = {
      nombre: '',
      apellidos: '',
      username: '',
      password: '',
      dni: '',
      especialidad: '' // Asegúrate de que este campo está vacío
    };
  }

  eliminarExperto(experto: any): void {
    console.log("Eliminar experto:", experto);

    // Confirmación antes de eliminar
    const confirmarEliminacion = confirm("¿Estás seguro de que deseas eliminar a este experto?");
    if (!confirmarEliminacion) {
      return;  // Si el usuario cancela, no hace nada
    }

    // Realizar la solicitud DELETE al backend
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.token,
      'Content-Type': 'application/json'
    });

    console.log('ID del experto:', experto.id || experto.idUser);  // Verifica que el id esté presente

    // Enviar la solicitud DELETE al backend
    this.http.delete(`http://localhost:8080/usuario/${experto.id || experto.idUser}`, { headers })
      .subscribe({
        next: (response: any) => {
          console.log('Respuesta del servidor al eliminar experto:', response);
          alert(response.message || "Experto eliminado con éxito.");
          this.obtenerExpertos();  // Actualizar la lista de expertos después de la eliminación
        },
        error: (error) => {
          console.error('Error al eliminar el experto', error);
          alert('Hubo un error al eliminar el experto.');
        }
      });
  }
}
