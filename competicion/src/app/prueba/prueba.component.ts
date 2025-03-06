import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PruebaService } from '../servicios/prueba.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import * as bootstrap from 'bootstrap';
import { ItemService } from '../servicios/item.service';

@Component({
  selector: 'app-prueba',
  templateUrl: './prueba.component.html',
  styleUrls: ['./prueba.component.css'],
  imports: [FormsModule, CommonModule]
})
export class PruebaComponent implements OnInit {

  prueba = {
    enunciado: '',
    puntuacionMaxima: 10,
    nombreEspecialidad: ''
  };

  especialidad: string = '';  // Especialidad que se mostrará en el formulario
  itemsList: any = [];
  itemsTemp: string[] = new Array(10).fill('');
  especialidadId: number = 0;
  constructor(
    private pruebaService: PruebaService,
    private router: Router,
    private itemService: ItemService,
  ) { }

  ngOnInit(): void {
    // Recupera la especialidad desde sessionStorage
    const especialidad = sessionStorage.getItem('especialidad');

    if (especialidad) {
      this.especialidad = especialidad;
      console.log("Especialidad del usuario:", this.especialidad);
    } else {
      console.error('Especialidad no encontrada');
    }
  }

  crearPrueba() {
    this.prueba.nombreEspecialidad = this.especialidad;

    this.pruebaService.crearPrueba(this.prueba).subscribe({
      next: (response: any) => {
        this.especialidadId = response;
        this.guardarItems()
      },
      error: (error: any) => {

      }
    });
  }



  abrirModal() {
    const modalElement = document.getElementById('modalItems');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }


  crearItems() {
    if (this.itemsTemp.every(item => item.trim() !== '')) {
      this.itemsList = this.itemsTemp.map((item: any) => {
        return {
          peso: 1,
          gradosConsecucion: 4,
          descripcion: item
        };
      });
      console.log(this.itemsList)
      const modalElement = document.getElementById('modalItems');
      if (modalElement) {
        const modal = bootstrap.Modal.getInstance(modalElement);
        modal!.hide(); // Cierra el modal
      }
    }
  }

  guardarItems() {
    this.itemService.guardarItems(this.itemsList, this.especialidadId).subscribe({
      next: (response: any) => {
        console.log('Items guardados con éxito', response);
        this.router.navigate(['/pruebas']);
      },
      error: (error: any) => {
        console.error('Hubo un error al guardar los items', error);
        console.error('Detalles del error:', error.error);
      }
    });
  }
}
