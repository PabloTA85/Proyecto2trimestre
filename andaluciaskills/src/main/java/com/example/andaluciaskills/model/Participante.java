package com.example.andaluciaskills.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Participante {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idParticipante;

  @NotBlank(message = "El nombre no puede estar vacío")
  @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
  private String nombre;

  @NotBlank(message = "Los apellidos no pueden estar vacíos")
  @Size(max = 150, message = "Los apellidos no pueden superar los 150 caracteres")
  private String apellidos;

  @NotBlank(message = "El email no puede estar vacío")
  @Email(message = "El email debe tener un formato válido")
  private String email;

  @NotBlank(message = "El centro no puede estar vacío")
  @Size(max = 200, message = "El centro no puede superar los 200 caracteres")
  private String centro;

  @ManyToOne
  @JoinColumn(name = "Especialidad_id")
  private Especialidad especialidad;

  // Constructor sin el id
  public Participante(String nombre, String apellidos, String email, String centro, Especialidad especialidad) {
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.email = email;
    this.centro = centro;
    this.especialidad = especialidad;
  }

  // Constructor vacío, necesario para JPA
  public Participante() {}
}
