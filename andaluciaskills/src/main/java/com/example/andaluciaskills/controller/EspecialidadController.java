package com.example.andaluciaskills.controller;

import com.example.andaluciaskills.converter.EspecialidadConverter;
import com.example.andaluciaskills.dto.EspecialidadDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.service.EspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/especialidad")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    // Obtener todas las especialidades
    @GetMapping("/all")
    @Operation(summary = "Obtener todas las especialidades", description = "Devuelve una lista de todas las especialidades")
    public List<EspecialidadDTO> getAllEspecialidades() {
        List<Especialidad> especialidades = especialidadService.findAll();
        return especialidades.stream()
                .map(EspecialidadConverter::toDTO)
                .toList();
    }

    // Obtener especialidad por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener especialidad por ID", description = "Devuelve los detalles de una especialidad dada su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    public ResponseEntity<EspecialidadDTO> getEspecialidadById(
            @Parameter(description = "ID de la especialidad", required = true) @PathVariable Long id) {
        Optional<Especialidad> especialidad = especialidadService.findById(id);
        return especialidad.map(e -> ResponseEntity.ok(EspecialidadConverter.toDTO(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva especialidad
    @PostMapping("/create")
    @Operation(summary = "Crear una nueva especialidad", description = "Permite crear una nueva especialidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error: código o nombre de especialidad duplicado")
    })
    public ResponseEntity<Map<String, String>> createEspecialidad(
            @Parameter(description = "Datos de la especialidad a crear", required = true) @RequestBody EspecialidadDTO especialidadDTO) {
        Especialidad especialidad = EspecialidadConverter.toEntity(especialidadDTO);

        if (especialidadService.findByCodigo(especialidad.getCodigo()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error: Ya existe una especialidad con el código " + especialidad.getCodigo()));
        }
        if (especialidadService.findByNombre(especialidad.getNombre()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error: Ya existe una especialidad con el nombre " + especialidad.getNombre()));
        }

        especialidadService.save(especialidad);
        return ResponseEntity.ok(Map.of("message", "Especialidad creada con éxito"));
    }

    // Actualizar una especialidad
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar especialidad", description = "Permite actualizar los datos de una especialidad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad actualizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error: código o nombre de especialidad duplicado"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    public ResponseEntity<String> updateEspecialidad(
            @Parameter(description = "ID de la especialidad", required = true) @PathVariable Long id,
            @Parameter(description = "Datos de la especialidad a actualizar", required = true) @RequestBody EspecialidadDTO especialidadDTO) {

        Optional<Especialidad> existingEspecialidadOpt = especialidadService.findById(id);
        if (!existingEspecialidadOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Especialidad especialidad = EspecialidadConverter.toEntity(especialidadDTO);
        especialidad.setIdEspecialidad(id);

        if (especialidadService.findByCodigo(especialidad.getCodigo()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Error: Ya existe una especialidad con el código " + especialidad.getCodigo());
        }
        if (especialidadService.findByNombre(especialidad.getNombre()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Error: Ya existe una especialidad con el nombre " + especialidad.getNombre());
        }

        especialidadService.save(especialidad);
        return ResponseEntity.ok("Especialidad actualizada con éxito");
    }

    // Eliminar una especialidad
    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar especialidad", description = "Permite eliminar una especialidad mediante su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    public ResponseEntity<String> deleteEspecialidad(
            @Parameter(description = "Código de la especialidad", required = true) @PathVariable String codigo) {
        // Buscar la especialidad por su código
        Optional<Especialidad> especialidad = especialidadService.findByCodigo(codigo);

        if (!especialidad.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Especialidad no encontrada con código: " + codigo);
        }

        try {
            // Eliminar la especialidad utilizando el ID
            especialidadService.delete(especialidad.get().getIdEspecialidad());
            return ResponseEntity.ok("Especialidad eliminada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la especialidad: " + e.getMessage());
        }
    }
}
