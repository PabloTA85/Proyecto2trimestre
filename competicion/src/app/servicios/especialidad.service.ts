import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginService } from './login.service'; // Asegúrate de que este servicio te permita obtener el token

@Injectable({
  providedIn: 'root'
})
export class EspecialidadService {
  private apiUrl = 'http://localhost:8080/api/especialidad/all'; // URL de tu API para obtener las especialidades

  constructor(private http: HttpClient, private loginService: LoginService) { }

  getEspecialidades(): Observable<any[]> {
    // Obtener el token desde el LoginService
    const token = this.loginService.getToken(); 

    // Configurar los encabezados de la solicitud, incluyendo el token de autorización
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    // Hacer la solicitud GET con los encabezados adecuados
    return this.http.get<any[]>(this.apiUrl, { headers });
  }
}
