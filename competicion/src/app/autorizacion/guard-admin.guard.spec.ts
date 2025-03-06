import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { guardAdminGuard } from './guard-admin.guard';
import { LoginService } from '../servicios/login.service';

describe('guardAdminGuard', () => {
  let guard: guardAdminGuard;
  let loginServiceMock: jasmine.SpyObj<LoginService>;
  let routerMock: jasmine.SpyObj<Router>;

  beforeEach(() => {
    // Crear mocks para las dependencias
    loginServiceMock = jasmine.createSpyObj('LoginService', ['isAdmin']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        guardAdminGuard,
        { provide: LoginService, useValue: loginServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    });

    // Inyectar el guard después de configurar el módulo
    guard = TestBed.inject(guardAdminGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should allow activation if user is admin', () => {
    loginServiceMock.isAdmin.and.returnValue(true); // Mockear que el usuario es admin

    const result = guard.canActivate();
    expect(result).toBe(true); // Se debe permitir la activación
    expect(routerMock.navigate).not.toHaveBeenCalled(); // No debe redirigir
  });

  it('should deny activation and redirect if user is not admin', () => {
    loginServiceMock.isAdmin.and.returnValue(false); // Mockear que el usuario no es admin

    const result = guard.canActivate();
    expect(result).toBe(false); // No se debe permitir la activación
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']); // Debe redirigir al login
  });
});
