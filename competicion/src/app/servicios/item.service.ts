import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginService } from './login.service';
import { Item } from '../interface/item.interface';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private apiUrl = 'http://localhost:8080/api/items'; // URL base de la API
  private token: string;
  private headers!: HttpHeaders;

  constructor(private http: HttpClient, private loginService: LoginService) {
    this.token = this.loginService.getToken();
    this.headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.token}`)
      .set('Content-Type', 'application/json');
  }
  obternerItems(): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiUrl}/all`, { headers: this.headers });
  }

  obternerItemsEspecialidad(especialidad: number): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiUrl}/especialidad/${especialidad}`, { headers: this.headers });
  }

  guardarItems(items: Item[], especialidad: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/create/${especialidad}`, items, { headers: this.headers });
  }

}
