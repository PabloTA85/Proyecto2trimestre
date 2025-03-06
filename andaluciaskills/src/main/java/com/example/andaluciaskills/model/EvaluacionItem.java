package com.example.andaluciaskills.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EvaluacionItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idEvaluacionItem;

  @ManyToOne
  @JoinColumn(name = "Evaluacion_id")
  @NotNull(message = "La evaluación no puede ser nula")  // Asegura que la evaluación no sea nula
  private Evaluacion evaluacion;

  @ManyToOne
  @JoinColumn(name = "Item_id")
  @NotNull(message = "El ítem no puede ser nulo")  // Asegura que el ítem no sea nulo
  private Item item;

  @Column(nullable = true)
  @Min(value = 1, message = "La valoración debe ser al menos 1")
  @Max(value = 10, message = "La valoración no puede ser mayor a 10")
  private int valoracion;

  @Size(max = 500, message = "El comentario no puede tener más de 500 caracteres")
  private String comentario;

  // Constructor sin el id
  public EvaluacionItem(Evaluacion evaluacion, Item item, int valoracion, String comentario) {
    this.evaluacion = evaluacion;
    this.item = item;
    this.valoracion = valoracion;
    this.comentario = comentario;
  }

  // Constructor vacío, necesario para JPA
  public EvaluacionItem() {}
}
