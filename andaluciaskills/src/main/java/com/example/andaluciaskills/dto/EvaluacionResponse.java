package com.example.andaluciaskills.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EvaluacionResponse {
    
    private double total;
    private String mensaje;
}
