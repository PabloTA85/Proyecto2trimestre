package com.example.andaluciaskills.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Evaluacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idEvaluacion;

  @ManyToOne
  @JoinColumn(name = "Participante_id")
  @NotNull(message = "El participante no puede ser nulo") // Validación de no nulo
  private Participante participante;

  @ManyToOne
  @JoinColumn(name = "Prueba_id")
  @NotNull(message = "La prueba no puede ser nula") // Validación de no nulo
  private Prueba prueba;

  @ManyToOne
  @JoinColumn(name = "User_id")
  @NotNull(message = "El evaluador no puede ser nulo") // Validación de no nulo
  private Usuario evaluador;

  @NotNull(message = "La nota final no puede ser nula") // Validación de no nulo
  @Min(value = 0, message = "La nota debe ser mayor o igual a 0") // Validación del rango mínimo
  @Max(value = 10, message = "La nota debe ser menor o igual a 10") // Validación del rango máximo
  private Float notaFinal;
}
