import { Component, OnInit } from '@angular/core';
import { NotasService } from '../servicios/notas.service';
import { HttpHeaders } from '@angular/common/http';  // Importamos HttpHeaders
import { CommonModule } from '@angular/common';  // Importamos el módulo CommonModule

@Component({
  selector: 'app-notas',
  templateUrl: './notas.component.html',
  styleUrls: ['./notas.component.css'],
  imports: [CommonModule]  // Importamos el módulo CommonModule
})
export class NotasComponent implements OnInit {
  evaluaciones: any[] = [];  // Arreglo para almacenar las evaluaciones
  token: string = 'tu-token-aqui';  // Reemplaza esto por tu método real para obtener el token

  constructor(private notasService: NotasService) { }

  ngOnInit(): void {
    this.obtenerEvaluaciones();
  }

  obtenerEvaluaciones(): void {
    console.log('Realizando solicitud para obtener todas las evaluaciones...');

    // Crear los headers con el token
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.token,  // Añadimos el token en los headers
      'Content-Type': 'application/json'
    });

    // Pasamos los headers al servicio
    this.notasService.getEvaluacionesConHeaders(headers).subscribe({
      next: (data: any[]) => {
        console.log('Datos recibidos en el componente:', data);  // Verifica los datos aquí
        if (data.length === 0) {
          alert('No se encontraron evaluaciones.');
        }
        this.evaluaciones = data;
      },
      error: (err: any) => {
        console.error('Error al obtener las evaluaciones', err);  // Imprime el error para entender qué está fallando
        alert('Ocurrió un error al obtener las evaluaciones');
      }
    });
  }
}
