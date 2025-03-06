import { RouterModule, Routes } from '@angular/router';
import { ListaCompetidoresComponent } from './lista-competidores/lista-competidores.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { guardAdminGuard } from './autorizacion/guard-admin.guard'
import { guardExpertoGuard } from './autorizacion/guard-experto.guard';
import { RegistroComponent } from './registro/registro.component';
import { NgModule } from '@angular/core';
import { ExpertoComponent } from './experto/experto.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EspecialidadComponent } from './especialidad/especialidad.component';
import { ParticipanteComponent } from './participante/participante.component';
import { EspecialidadExpertoComponent } from './especialidad-experto/especialidad-experto.component';
import { EvaluacionComponent } from './evaluacion/evaluacion.component';
import { PuntuacionComponent } from './puntuacion/puntuacion.component';
import { ExpertoParticipanteComponent } from './experto-participante/experto-participante.component';
import { PruebaComponent } from './prueba/prueba.component';
import { ItemComponent } from './item/item.component';
import { NotasComponent } from './notas/notas.component';


export const routes: Routes = [
    { path: '', component: ListaCompetidoresComponent, pathMatch: 'full' },
    { path: 'login', component: LoginComponent, pathMatch: 'full' },
    {
      path: 'admin',
      canActivate: [guardAdminGuard],
      children: [
        { path: 'experto', component: ExpertoComponent, pathMatch: 'full' },
        { path: 'especialidad', component: EspecialidadComponent, pathMatch: 'full' },
        { path: 'participante', component: ParticipanteComponent, pathMatch: 'full' },
      ]
    },
    {
      path: 'experto',
      canActivate: [guardExpertoGuard],
      children: [
        { path: 'especialidad-experto', component: EspecialidadExpertoComponent, pathMatch: 'full' },
        { path: 'experto-participante', component: ExpertoParticipanteComponent, pathMatch: 'full' },
        { path: 'prueba', component: PruebaComponent, pathMatch: 'full' },
        { path: 'evaluacion', component: EvaluacionComponent, pathMatch: 'full' },
        { path: 'item', component: ItemComponent, pathMatch: 'full' },
        { path: 'notas', component: NotasComponent, pathMatch: 'full' },
      ]
    },
    { path: 'registro', component: RegistroComponent, pathMatch: 'full' },
  ];
  

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule { }
