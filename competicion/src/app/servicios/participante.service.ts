import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ParticipanteService {
  private apiUrl = 'http://localhost:8080/api/participante'; // URL base de la API

  constructor(private http: HttpClient) {}

  // Obtener todos los participantes
  obtenerParticipantes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`);
  }

  // Agregar un nuevo participante
  agregarParticipante(participante: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add`, participante);
  }

  // Modificar un participante existente
  modificarParticipante(participante: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update`, participante);
  }

  // Eliminar un participante por email
  eliminarParticipante(email: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/delete/${email}`);
  }
}
