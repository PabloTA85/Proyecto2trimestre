import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PruebaService {

  private apiUrl = 'http://localhost:8080/api/pruebas/';

  constructor(private http: HttpClient) { }

  crearPrueba(prueba: any): Observable<any> {
    // Asegurarse de que estamos enviando la solicitud con los headers correctos
    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    // Enviar la solicitud con las cabeceras configuradas
    return this.http.post(this.apiUrl + 'create', prueba, { headers });
  }
}
