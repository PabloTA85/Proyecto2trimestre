package com.example.andaluciaskills.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipanteDTO {
    private Long idParticipante;
    private String nombre;
    private String apellidos;
    private String email;
    private String centro;
    private String nombreEspecialidad;
}
