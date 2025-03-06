package com.example.andaluciaskills.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PruebaDTO {
    private Long idPrueba;
    private String enunciado;
    private int puntuacionMaxima;
    private String nombreEspecialidad; // Para vincularlo a una Especialidad específica
    private byte[] pdfData;
}


    