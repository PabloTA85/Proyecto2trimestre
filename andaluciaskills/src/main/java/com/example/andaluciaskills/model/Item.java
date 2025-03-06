package com.example.andaluciaskills.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idItem;

  @NotBlank(message = "La descripción no puede estar vacía")
  private String descripcion;

  @Min(value = 1, message = "El peso debe ser 1")
  private int peso;

  @Min(value = 0, message = "Los grados de consecución deben ser mayores o iguales a 0")
  private int gradosConsecucion;  

  @ManyToOne
  @JoinColumn(name = "Prueba_id")
  @JsonIgnore
  @NotNull(message = "La prueba no puede ser nula")  // Asegura que la prueba esté presente
  private Prueba prueba;

  // Constructor sin el id
  public Item(String descripcion, int peso, int gradosConsecucion, Prueba prueba) {
    this.descripcion = descripcion;
    this.peso = peso;
    this.gradosConsecucion = gradosConsecucion;
    this.prueba = prueba;
  }

  // Constructor vacío, necesario para JPA
  public Item() {}
}
