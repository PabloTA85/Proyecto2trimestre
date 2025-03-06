import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

interface Prueba {
  id: number;
  nombre: string;
  descripcion: string;
  // Add other properties as needed
}

@Injectable({
  providedIn: 'root',
})
export class EvaluacionService {
  private apiUrl = 'http://localhost:8080/api/pruebas/all';  // URL para obtener todas las pruebas
  private evaluarUrl = 'http://localhost:8080/api/evaluacion-items';  // URL para evaluar una prueba
  private evaluacionesUrl = 'http://localhost:8080/api/evaluaciones';  // URL para evaluaciones

  private token: string;

  constructor(private http: HttpClient) {
    this.token = ''; // Initialize the token, you can set it to a default value or fetch it from a secure source
  }

  // Método para obtener todas las pruebas
  getPruebas(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Método para obtener una prueba por ID
  getPruebaById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Método para obtener pruebas por especialidad
  getPruebasPorEspecialidad(usuario: string, token: string): Observable<any[]> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json');

    return this.http.get<any[]>(`http://localhost:8080/api/pruebas/porEspecialidad/${usuario}`, { headers });
  }

  // Método para obtener participantes por especialidad
  getParticipantesPorEspecialidad(usuario: string, token: string): Observable<any[]> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json');

    return this.http.get<any[]>(`http://localhost:8080/api/participante/porEspecialidad/${usuario}`, { headers });
  }

  // Método para enviar la evaluación al backend
  evaluarPrueba(items: any[]): Observable<any> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.token}`)
      .set('Content-Type', 'application/json');
    return this.http.post<any>(`${this.evaluarUrl}/crear`, items);
  }


  crearEvaluacion(evaluacion: any): Observable<any> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.token}`)
      .set('Content-Type', 'application/json');
    return this.http.post(`${this.evaluacionesUrl}/crear`, evaluacion, { headers });

  }

  crearPrueba(prueba: Prueba): Observable<Prueba> {
    return this.http.post<Prueba>(this.apiUrl, prueba);
  }

  evaluarItems(evaluacion: any): Observable<any> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.token}`)
      .set('Content-Type', 'application/json');
    return this.http.post(`${this.evaluarUrl}/crear`, evaluacion, { headers });
  }
}
