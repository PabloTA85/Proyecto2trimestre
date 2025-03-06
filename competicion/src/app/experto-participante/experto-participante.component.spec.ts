import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpertoParticipanteComponent } from './experto-participante.component';

describe('ExpertoParticipanteComponent', () => {
  let component: ExpertoParticipanteComponent;
  let fixture: ComponentFixture<ExpertoParticipanteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpertoParticipanteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExpertoParticipanteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
