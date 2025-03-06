package com.example.andaluciaskills.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluacionItemDTO {
    
    private Long evaluacionId;
    private Long participanteId;
    private Long pruebaId;
    private List<ItemDTO> items;
}
