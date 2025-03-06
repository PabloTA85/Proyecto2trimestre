package com.example.andaluciaskills.controller;

import com.example.andaluciaskills.converter.EvaluacionConverter;
import com.example.andaluciaskills.dto.EvaluacionDTO;
import com.example.andaluciaskills.service.EvaluacionService;
import com.example.andaluciaskills.model.Evaluacion;
import com.example.andaluciaskills.service.ParticipanteService;
import com.example.andaluciaskills.service.PruebaService;
import com.example.andaluciaskills.service.UsuarioService;
import com.example.andaluciaskills.model.Participante;
import com.example.andaluciaskills.model.Prueba;
import com.example.andaluciaskills.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @Autowired
    private EvaluacionConverter evaluacionConverter;

    @Autowired
    private ParticipanteService participanteService;

    @Autowired
    private PruebaService pruebaService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todas las evaluaciones", description = "Obtiene una lista de todas las evaluaciones registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de evaluaciones encontrada"),
        @ApiResponse(responseCode = "204", description = "No hay evaluaciones disponibles"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/all")
    public ResponseEntity<List<EvaluacionDTO>> getAllEvaluaciones() {
        try {
            List<Evaluacion> evaluaciones = evaluacionService.findAll();
            if (evaluaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<EvaluacionDTO> evaluacionDTOs = evaluaciones.stream()
                    .map(evaluacion -> evaluacionConverter.toDTO(evaluacion))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(evaluacionDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener una evaluación por su ID", description = "Obtiene una evaluación por su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluación encontrada"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> getEvaluacionById(@Parameter(description = "ID de la evaluación a obtener") @PathVariable Long id) {
        Optional<Evaluacion> evaluacionOptional = evaluacionService.findById(id);
        if (evaluacionOptional.isPresent()) {
            EvaluacionDTO dto = evaluacionConverter.toDTO(evaluacionOptional.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Crear una nueva evaluación", description = "Crea una nueva evaluación y la almacena en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evaluación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la evaluación inválidos")
    })
    @PostMapping("/crear")
    public ResponseEntity<EvaluacionDTO> createEvaluacion(@RequestBody EvaluacionDTO evaluacionDTO) {
        Participante participante = obtenerParticipantePorId(evaluacionDTO.getParticipanteId());
        Prueba prueba = obtenerPruebaPorId(evaluacionDTO.getPruebaId());
        Usuario evaluador = obtenerUsuarioPorId(evaluacionDTO.getEvaluadorId());
        Evaluacion evaluacion = evaluacionConverter.toEntity(evaluacionDTO, participante, prueba, evaluador);
        Evaluacion evaluacionGuardada = evaluacionService.save(evaluacion);
        EvaluacionDTO dto = evaluacionConverter.toDTO(evaluacionGuardada);
        dto.setIdEvaluacion(evaluacionGuardada.getIdEvaluacion());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener evaluaciones por usuario", description = "Obtiene las evaluaciones relacionadas con un usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas para el usuario"),
        @ApiResponse(responseCode = "204", description = "No hay evaluaciones disponibles para el usuario"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/porUsuario/{idUser}")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesPorUsuario(@Parameter(description = "ID del usuario para obtener sus evaluaciones") @PathVariable Long userId) {
        try {
            List<Evaluacion> evaluaciones = evaluacionService.getEvaluacionesPorUsuario(userId);
            if (!evaluaciones.isEmpty()) {
                List<EvaluacionDTO> evaluacionDTOs = evaluaciones.stream()
                        .map(evaluacion -> {
                            EvaluacionDTO dto = new EvaluacionDTO();
                            dto.setIdEvaluacion(evaluacion.getIdEvaluacion());
                            dto.setParticipanteId(evaluacion.getParticipante().getIdParticipante());
                            dto.setEvaluadorId(evaluacion.getEvaluador().getIdUser());
                            dto.setPruebaId(evaluacion.getPrueba().getIdPrueba());
                            dto.setNotaFinal(evaluacion.getNotaFinal());
                            return dto;
                        })
                        .collect(Collectors.toList());
                return ResponseEntity.ok(evaluacionDTOs);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Participante obtenerParticipantePorId(Long id) {
        return participanteService.findById(id).orElseThrow(() -> new RuntimeException("Participante no encontrado"));
    }

    private Prueba obtenerPruebaPorId(Long id) {
        return pruebaService.findById(id).orElseThrow(() -> new RuntimeException("Prueba no encontrada"));
    }

    private Usuario obtenerUsuarioPorId(Long id) {
        return usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
