import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CommonModule } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  token: string;
  perfil: string;
  logeado: boolean;
  usuario: any;
  private especialidades: string = "";

  constructor(private http: HttpClient) {
    this.token = '';
    this.perfil = '';
    this.logeado = false;
    this.usuario = {};
    this.recuperar();  // Llamamos a recuperar los datos cuando se instancia el servicio
  }

  private almacenar() {
    const objeto: any = {
      token: this.token,
      perfil: this.perfil,
      logeado: this.logeado,
      usuario: this.usuario
    };
    try {
      sessionStorage.setItem('LOGIN', JSON.stringify(objeto));  // Intentamos almacenar en sessionStorage
    } catch (error) {
      console.error('Error al almacenar los datos en sessionStorage:', error);
    }
  }

  getToken() {
    const cadena = sessionStorage.getItem('LOGIN');
    if (cadena) {
      try {
        const objeto = JSON.parse(cadena);  // Intentamos parsear los datos almacenados
        return objeto.token;
      } catch (error) {
        console.error('Error al obtener el token:', error);
      }
    }
  }

  recuperar() {
    const cadena: any = sessionStorage.getItem('LOGIN');
    console.log(cadena)
    if (cadena) {
      try {
        const objeto = JSON.parse(cadena);  // Intentamos parsear los datos almacenados
        this.token = objeto.token;
        this.perfil = objeto.perfil;
        this.logeado = objeto.logeado;
        this.usuario = objeto.usuario;
      } catch (error) {
        console.error('Error al parsear los datos de sessionStorage:', error);
        // Si hay un error al parsear, limpiamos sessionStorage
        sessionStorage.removeItem('LOGIN');
      }
    } else {
      this.token = '';
      this.perfil = '';
      this.logeado = false;
      this.usuario = {};
    }
  }

  login(user: string, pass: string) {
    let objeto: any = this;
    return this.http.post('http://localhost:8080/api/auth/login', { username: user, password: pass })
      .pipe(
        map((data: any) => {
          console.log('Respuesta del login:', data); // Verifica la respuesta completa
          if (data && data.token) {
            objeto.usuario = {
              idUser: data.idUser,
              nombre: data.username,
              especialidad: data.especialidad  // Asegúrate de que este campo esté en la respuesta
            };
            console.log('Especialidad:', data.especialidad);
            objeto.perfil = data.role;
            objeto.token = data.token;
            objeto.logeado = true;

            // Almacenar la especialidad
            sessionStorage.setItem('especialidad', data.especialidad);

            objeto.almacenar();

            return { "funciona": true, "perfil": data.role };
          } else {
            return { "funciona": false };
          }
        })
      );
  }

  private machacar() {
    sessionStorage.removeItem('LOGIN');  // Eliminar los datos de sesión
  }

  logout() {
    let objeto: any = this;
    let cont: string | null = sessionStorage.getItem('LOGIN');
    if (cont) {
      this.http.get("http://localhost/Cliente/Angular/servidorSkills/logout.php?desloguear=" + JSON.parse(cont).token)
        .subscribe(() => {
          objeto.machacar();
        });
    }
    this.token = '';
    this.perfil = '';
    this.logeado = false;
    this.usuario = {};
    sessionStorage.removeItem('LOGIN');  // Limpiar sesión
    sessionStorage.removeItem('especialidad');  // Asegúrate de limpiar la especialidad también
  }

  isLogged(): boolean {
    let respuesta: boolean = false;
    let cont: string | null = sessionStorage.getItem('LOGIN');
    if (cont) {
      try {
        respuesta = JSON.parse(cont).logeado;
      } catch (e) {
        console.error('Error al verificar sesión:', e);
      }
    }
    return respuesta;
  }

  getNombre() {
    let respuesta: string = "";
    let cont: string | null = sessionStorage.getItem('LOGIN');
    if (cont) {
      try {
        respuesta = JSON.parse(cont).usuario.nombre;
      } catch (e) {
        console.error('Error al obtener nombre:', e);
      }
    }
    return respuesta;
  }

  getIdUser() {
    let respuesta: string = "";
    let cont: string | null = sessionStorage.getItem('LOGIN');
    if (cont) {
      try {
        respuesta = JSON.parse(cont).usuario.idUser;
      } catch (e) {
        console.error('Error al obtener idUser:', e);
      }
    }
    return respuesta;
  }

  getPerfil() {
    let respuesta: string = "";
    let cont: string | null = sessionStorage.getItem('LOGIN');
    if (cont) {
      try {
        respuesta = JSON.parse(cont).perfil;
      } catch (e) {
        console.error('Error al obtener perfil:', e);
      }
    }
    return respuesta;
  }

  registrar(user: string, pass: string, correo: string): Observable<any> {
    return this.http.post('http://localhost/Cliente/Angular/servidorSkills/registro.php',
      { user: user, pass: pass, correo: correo }
    ).pipe(
      map((data: any) => {
        return data != null && data.success ? { "funciona": true } : { "funciona": false };
      })
    );
  }

  isAdmin(): boolean {
    return typeof this.perfil === 'string' && this.perfil.includes('ROLE_ADMIN');
  }

  isExperto(): boolean {
    // Asegúrate de que 'this.perfil' es un string antes de llamar a 'includes'
    return typeof this.perfil === 'string' && this.perfil.includes('ROLE_EXPERTO');
  }

  // Asegúrate de que se esté guardando correctamente en el servicio.
  setEspecialidad(especialidad: string) {
    this.especialidades = especialidad;
    sessionStorage.setItem('especialidad', especialidad); // Guarda la especialidad en sessionStorage
  }

  getEspecialidad(): Observable<string> {
    return new Observable(observer => {
      const especialidad = sessionStorage.getItem('especialidad'); // Obtén la especialidad desde sessionStorage
      if (especialidad) {
        observer.next(especialidad);
      } else {
        observer.error('Especialidad no encontrada');
      }
    });
  }
}
