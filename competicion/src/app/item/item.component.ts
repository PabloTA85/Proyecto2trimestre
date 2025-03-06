import { Component, OnInit } from '@angular/core';
import { ItemService } from '../servicios/item.service';
import { Item } from '../interface/item.interface';

@Component({
  selector: 'app-item',
  imports: [],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css'
})
export class ItemComponent implements OnInit {
  items: Item[] = [];
  constructor(private itemService: ItemService) { }

  ngOnInit(): void {

    this.itemService.obternerItemsEspecialidad(2).subscribe(data => {
      this.items = data;
      console.log("Items obtenidos:", data);
    });
  }

}
