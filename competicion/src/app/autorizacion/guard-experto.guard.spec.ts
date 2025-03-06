import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { guardExpertoGuard } from './guard-experto.guard';
import { LoginService } from '../servicios/login.service';

describe('GuardExpertoGuard', () => {
  let guard: guardExpertoGuard;
  let loginServiceSpy: jasmine.SpyObj<LoginService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    const spyLogin = jasmine.createSpyObj('LoginService', ['isExperto']);
    const spyRouter = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        guardExpertoGuard,
        { provide: LoginService, useValue: spyLogin },
        { provide: Router, useValue: spyRouter }
      ]
    });
    guard = TestBed.inject(guardExpertoGuard);
    loginServiceSpy = TestBed.inject(LoginService) as jasmine.SpyObj<LoginService>;
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('debería permitir la activación cuando el usuario es experto', () => {
    loginServiceSpy.isExperto.and.returnValue(true);
    expect(guard.canActivate()).toBeTrue();
  });

  it('debería bloquear la activación y redirigir cuando el usuario no es experto', () => {
    loginServiceSpy.isExperto.and.returnValue(false);
    expect(guard.canActivate()).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
