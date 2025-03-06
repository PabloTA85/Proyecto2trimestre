package com.example.andaluciaskills.controller;

import com.example.andaluciaskills.converter.ParticipanteConverter;
import com.example.andaluciaskills.dto.ParticipanteDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Participante;
import com.example.andaluciaskills.model.Usuario;
import com.example.andaluciaskills.service.EspecialidadService;
import com.example.andaluciaskills.service.ParticipanteService;
import com.example.andaluciaskills.service.UsuarioService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/participante")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EspecialidadService especialidadService;

    // Obtener todos los participantes como DTOs
    @Operation(summary = "Obtener todos los participantes", description = "Este endpoint devuelve todos los participantes como DTOs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participantes obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ParticipanteDTO>> getAllParticipantes() {
        List<Participante> participantes = participanteService.findAll();
        List<ParticipanteDTO> dtos = participantes.stream().map(participante -> {
            ParticipanteDTO dto = new ParticipanteDTO();
            dto.setIdParticipante(participante.getIdParticipante());
            dto.setNombre(participante.getNombre());
            dto.setApellidos(participante.getApellidos());
            dto.setEmail(participante.getEmail());
            dto.setCentro(participante.getCentro());
            dto.setNombreEspecialidad(participante.getEspecialidad().getNombre());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Obtener un participante por ID como DTO
    @Operation(summary = "Obtener participante por ID", description = "Este endpoint devuelve un participante específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participante encontrado"),
            @ApiResponse(responseCode = "404", description = "Participante no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteDTO> getParticipanteById(
            @Parameter(description = "ID del participante a obtener") @PathVariable Long id) {
        Participante participante = participanteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));
        ParticipanteDTO participanteDTO = ParticipanteConverter.toDTO(participante);
        return ResponseEntity.ok(participanteDTO);
    }

    // Crear un participante a partir de un DTO
    @Operation(summary = "Crear un nuevo participante", description = "Este endpoint crea un nuevo participante a partir de los datos proporcionados en un DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participante creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createParticipante(@RequestBody ParticipanteDTO participanteDTO) {
        Especialidad especialidad = especialidadService
                .findByNombre(participanteDTO.getNombreEspecialidad())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        Participante participante = ParticipanteConverter.toEntity(participanteDTO, especialidad);
        participanteService.save(participante);

        return ResponseEntity.ok("Participante creado exitosamente");
    }

    @Operation(summary = "Crear un participante por especialidad", description = "Este endpoint crea un participante asociado a la especialidad de un usuario, identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participante creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o error en la especialidad")
    })
    @PostMapping("/create/byEspecialidad/{idUser}")
    public ResponseEntity<Map<String, Object>> createParticipanteByEspecialidad(
            @RequestBody ParticipanteDTO participanteDTO,
            @PathVariable Long idUser) {

        Map<String, Object> response = new HashMap<>();

        // Verificar que todos los campos están completos
        if (participanteDTO == null || participanteDTO.getNombre() == null || participanteDTO.getApellidos() == null ||
                participanteDTO.getEmail() == null || participanteDTO.getCentro() == null) {
            response.put("status", "error");
            response.put("message", "Por favor, completa todos los campos del participante.");
            return ResponseEntity.badRequest().body(response);
        }

        // Buscar el usuario por su ID
        Usuario usuario = usuarioService.findById(idUser).orElse(null);
        if (usuario == null) {
            response.put("status", "error");
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Obtener la especialidad del usuario
        Especialidad especialidad = usuario.getEspecialidad();
        if (especialidad == null) {
            response.put("status", "error");
            response.put("message", "El usuario no tiene una especialidad asignada");
            return ResponseEntity.badRequest().body(response);
        }

        // Crear el participante
        Participante participante = new Participante();
        participante.setNombre(participanteDTO.getNombre());
        participante.setApellidos(participanteDTO.getApellidos());
        participante.setEmail(participanteDTO.getEmail());
        participante.setCentro(participanteDTO.getCentro());
        participante.setEspecialidad(especialidad);

        try {
            participanteService.save(participante);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al guardar el participante.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Respuesta exitosa
        response.put("status", "success");
        response.put("message", "Participante creado exitosamente con la especialidad del usuario logeado");
        return ResponseEntity.ok(response);
    }

    @Operation(summary =  "Actualizar un participante por ID", description = "Permite actualizar la información de un participante incluyendo su especialidad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participante actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Participante no encontrado o especialidad no válida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateParticipante(@PathVariable Long id,
            @RequestBody ParticipanteDTO participanteDTO) {
        try {
            // Buscar al participante por id
            Participante participante = participanteService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Participante no encontrado"));

            // Si el nombre de la especialidad ha cambiado, actualízalo
            if (participanteDTO.getNombreEspecialidad() != null && !participanteDTO.getNombreEspecialidad().isEmpty()) {
                // Buscar la especialidad
                Especialidad especialidad = especialidadService
                        .findByNombre(participanteDTO.getNombreEspecialidad())
                        .orElseThrow(() -> new EntityNotFoundException("Especialidad con el nombre "
                                + participanteDTO.getNombreEspecialidad() + " no encontrada"));

                // Actualizar la especialidad
                participante.setEspecialidad(especialidad);
            }

            // Actualizar los datos del participante con los nuevos valores
            participante.setNombre(participanteDTO.getNombre());
            participante.setApellidos(participanteDTO.getApellidos());
            participante.setEmail(participanteDTO.getEmail());
            participante.setCentro(participanteDTO.getCentro());

            // Guardar el participante actualizado
            participanteService.save(participante);

            // Responder con un mensaje JSON
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Participante actualizado exitosamente");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al modificar el participante"));
        }
    }

    @Operation(summary = "Eliminar un participante por ID", description = "Permite eliminar un participante utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "204", description = "Participante eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Participante no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipante(@PathVariable Long id) {
        if (!participanteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        participanteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener los participantes por especialidad del usuario logueado", description = "Obtiene los participantes que tienen la misma especialidad que el experto logueado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de participantes obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud, el usuario no tiene especialidad"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "204", description = "No hay participantes para esta especialidad")
    })
    @GetMapping("/porEspecialidad/{username}")
    public ResponseEntity<List<Participante>> getParticipantesByEspecialidad(@PathVariable String username) {
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Especialidad especialidadExperto = usuario.getEspecialidad();
        if (especialidadExperto != null) {
            List<Participante> participantes = participanteService
                    .findByEspecialidad(especialidadExperto.getIdEspecialidad());
            return participantes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(participantes);
        }

        return ResponseEntity.badRequest().build();
    }
}