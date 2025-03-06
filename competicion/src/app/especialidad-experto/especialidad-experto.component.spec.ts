import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EspecialidadExpertoComponent } from './especialidad-experto.component';

describe('EspecialidadExpertoComponent', () => {
  let component: EspecialidadExpertoComponent;
  let fixture: ComponentFixture<EspecialidadExpertoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EspecialidadExpertoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EspecialidadExpertoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
