package com.example.andaluciaskills.controller;

import com.example.andaluciaskills.dto.EvaluacionItemDTO;
import com.example.andaluciaskills.dto.EvaluacionResponse;
import com.example.andaluciaskills.service.EvaluacionItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluacion-items")
public class EvaluacionItemController {

    @Autowired
    private EvaluacionItemService evaluacionItemService;

    @Operation(summary = "Crear un nuevo ítem de evaluación", description = "Crea un nuevo ítem de evaluación y lo almacena en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítem de evaluación creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la evaluación inválidos")
    })
    @PostMapping("/crear")
    public ResponseEntity<EvaluacionResponse> create(@RequestBody EvaluacionItemDTO evaluacionItemDTO) {
        try {
            var response = evaluacionItemService.evaluar(evaluacionItemDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
