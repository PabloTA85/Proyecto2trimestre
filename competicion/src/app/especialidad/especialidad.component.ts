import { Component, OnInit } from '@angular/core';
import { EspecialidadService } from '../servicios/especialidad.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-especialidad', // Componente que se encuentra en el directorio especialidad
  imports: [CommonModule, FormsModule], // Importamos los módulos necesarios      
  templateUrl: './especialidad.component.html', // En el archivo especialidad.component.html
  styleUrls: ['./especialidad.component.css'] // En el archivo especialidad.component.css
})
export class EspecialidadComponent implements OnInit {
  agregarEspecialidad(): void {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.token,
      'Content-Type': 'application/json'
    });

    this.http.post('http://localhost:8080/api/especialidad/create', this.nuevaEspecialidad, { headers })
      .subscribe({
        next: (response: any) => {
          console.log('Respuesta del servidor:', response);

          // Verificar si la respuesta contiene el campo "message"
          if (response && response.message) {
            alert(response.message);  // Mostrar el mensaje recibido
          } else {
            alert('Especialidad creada con éxito.');
          }

          this.obtenerEspecialidades();  // Actualizar la lista de especialidades
        },
        error: (error: any) => {
          console.error('Error al crear especialidad', error);
          alert(error.error?.message || 'Hubo un error al crear la especialidad.');
        }
      });
  }

  // Declaración de nuevaEspecialidad para usar en el formulario
  nuevaEspecialidad = {
    codigo: '',
    nombre: ''
  };

  especialidades: any[] = []; // Array que almacenará las especialidades

  token: string = 'tu-token-aqui'; // Asigna el token de autenticación

  constructor(private especialidadService: EspecialidadService, private http: HttpClient) { }

  ngOnInit(): void {
    this.obtenerEspecialidades(); // Llamamos al método obtenerEspecialidades() al iniciar el componente
  }

  obtenerEspecialidades(): void {
    // Llamamos al método getEspecialidades() del servicio especialidad
    this.especialidadService.getEspecialidades()
      .subscribe(
        (data) => {
          this.especialidades = data; // Asignamos los datos obtenidos al array especialidades
        },
        (error) => {
          console.error('Error al obtener las especialidades', error);
        }
      );
  }

  eliminarEspecialidad(codigo: string): void {
    const confirmarEliminacion = confirm("¿Estás seguro de que deseas eliminar esta especialidad?");
    if (!confirmarEliminacion) {
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.token,
      'Content-Type': 'application/json'
    });

    // Realizamos la solicitud DELETE usando el código de la especialidad
    this.http.delete(`http://localhost:8080/api/especialidad/${codigo}`, { headers })
      .subscribe({
        next: (response: any) => {
          // Verificar la respuesta completa, incluyendo el código de estado
          console.log('Respuesta recibida:', response);
          alert(response || "Especialidad eliminada con éxito.");
          this.obtenerEspecialidades();  // Actualizamos la lista de especialidades
        },
        error: (error: any) => {
          // Depurar la respuesta de error
          console.error('Error al eliminar la especialidad', error);
          if (error.status === 200) {
            alert("Especialidad eliminada con éxito.");
          } else {
            alert('Hubo un error al eliminar la especialidad.');
          }
        }
      });
  }


}
