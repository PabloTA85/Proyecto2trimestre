import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotasService {
  private apiUrl = 'http://localhost:8080/api/evaluaciones/all';  

  constructor(private http: HttpClient) {}

  // Recibimos los headers como parámetro
  getEvaluacionesConHeaders(headers: HttpHeaders): Observable<any[]> {  
    return this.http.get<any[]>(this.apiUrl, { headers }).pipe(
      tap(response => console.log("Datos recibidos en el servicio:", response)) // Verifica los datos aquí
    );
  }
}
