package com.example.andaluciaskills.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluacionDTO {
    private Long idEvaluacion;
    private Long participanteId;
    private Long evaluadorId;
    private Long pruebaId;
    private Float notaFinal;
}
