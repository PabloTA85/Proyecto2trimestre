package com.example.andaluciaskills.controller;

import com.example.andaluciaskills.converter.PruebaConverter;
import com.example.andaluciaskills.dto.PruebaDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Item;
import com.example.andaluciaskills.model.Prueba;
import com.example.andaluciaskills.model.Usuario;
import com.example.andaluciaskills.repository.PruebaRepository;
import com.example.andaluciaskills.service.EspecialidadService;
import com.example.andaluciaskills.service.PruebaService;
import com.example.andaluciaskills.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pruebas")
public class PruebaController {

    @Autowired
    private PruebaService pruebaService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private PruebaRepository pruebaRepository;

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todas las pruebas
    @GetMapping("/all")
    public ResponseEntity<List<Prueba>> getAllPruebas() {
        List<Prueba> pruebas = pruebaService.findAll();
        return ResponseEntity.ok(pruebas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prueba> getPruebaById(@PathVariable Long id) {
        Optional<Prueba> pruebaOpt = pruebaService.findById(id);

        if (pruebaOpt.isPresent()) {
            Prueba prueba = pruebaOpt.get();
            // Aquí ya tendrías la URL del PDF dentro del objeto 'prueba'
            return ResponseEntity.ok(prueba); // La URL se incluiría en la respuesta si está en el modelo.
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPrueba(@RequestBody PruebaDTO pruebaDTO) {
        // Obtener la especialidad usando el nombre desde el DTO
        Optional<Especialidad> especialidadOpt = especialidadService.findByNombre(pruebaDTO.getNombreEspecialidad());

        // Verificar que la especialidad existe
        if (!especialidadOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Error: Especialidad no encontrada");
        }

        Especialidad especialidad = especialidadOpt.get();

        // Convertir DTO a entidad, pasando también la especialidad
        Prueba prueba = PruebaConverter.toEntity(pruebaDTO, especialidad);

        // Validar si ya existe una prueba con el mismo enunciado
        if (pruebaService.findByEnunciado(prueba.getEnunciado()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Error: Ya existe una prueba con el enunciado " + prueba.getEnunciado());
        }

        // Guardar la entidad usando el servicio
        prueba = pruebaService.save(prueba);

        // Devolver un mensaje de éxito con el ID de la prueba
        return ResponseEntity.ok(prueba.getIdPrueba().toString());
    }

    // Actualizar una prueba por ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePrueba(@PathVariable Long id, @RequestBody PruebaDTO pruebaDTO) {
        try {
            Especialidad especialidad = especialidadService.findByNombre(pruebaDTO.getNombreEspecialidad())
                    .orElseThrow(() -> new RuntimeException(
                            "Especialidad no encontrada con nombre: " + pruebaDTO.getNombreEspecialidad()));

            Prueba pruebaExistente = pruebaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prueba no encontrada con ID: " + id));

            // Actualizar los campos de la prueba existente con los datos del DTO
            Prueba pruebaActualizada = PruebaConverter.toEntity(pruebaDTO, especialidad);
            pruebaActualizada.setIdPrueba(pruebaExistente.getIdPrueba());

            pruebaService.save(pruebaActualizada);
            return ResponseEntity.ok("Prueba actualizada exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(400).body("Error al actualizar la prueba: " + ex.getMessage());
        }
    }

    // Eliminar una prueba por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrueba(@PathVariable Long id) {
        try {
            Prueba prueba = pruebaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prueba no encontrada con ID: " + id));

            pruebaService.delete(prueba.getIdPrueba());
            return ResponseEntity.ok("Prueba eliminada exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(400).body("Error al eliminar la prueba: " + ex.getMessage());
        }
    }

    // Obtener los items asociados a una prueba por ID
    @GetMapping("/{id}/items")
    public ResponseEntity<List<Item>> getItemsByPruebaId(@PathVariable Long id) {
        try {
            List<Item> items = pruebaService.findItemsByPruebaId(id);
            return ResponseEntity.ok(items);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(400).body(List.of());
        }
    }

    @PostMapping("/{idPrueba}/subir-pdf")
    public ResponseEntity<?> subirPdf(@PathVariable Long idPrueba,
            @RequestParam("archivoPdf") MultipartFile archivoPdf) {
        try {
            // Validar si el archivo es un PDF
            if (archivoPdf.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo PDF está vacío.");
            }
            if (!archivoPdf.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body("El archivo debe ser un PDF.");
            }

            // Llamada al servicio para guardar el PDF
            Prueba pruebaActualizada = pruebaService.guardarPdf(idPrueba, archivoPdf);

            // Retorno con la respuesta positiva
            return ResponseEntity.ok(pruebaActualizada);
        } catch (IOException e) {
            // Manejo de excepción IO
            return ResponseEntity.status(500).body("Error al procesar el archivo PDF: " + e.getMessage());
        } catch (RuntimeException e) {
            // Manejo de excepción de tipo Runtime (por ejemplo, prueba no encontrada)
            return ResponseEntity.status(404).body("Prueba no encontrada: " + e.getMessage());
        }
    }

    @GetMapping("/porEspecialidad/{username}")
    public ResponseEntity<List<Prueba>> getPruebasByEspecialidad(@PathVariable String username) {
        // Buscar al usuario por su username
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener la especialidad del usuario
        Especialidad especialidadExperto = usuario.getEspecialidad();

        // Verificar si el usuario tiene especialidad asignada
        if (especialidadExperto != null) {
            // Obtener la lista de pruebas asociadas con la especialidad
            List<Prueba> pruebas = pruebaService.findByEspecialidad(especialidadExperto.getIdEspecialidad()); // Cambiado
                                                                                                              // aquí

            // Verificar si existen pruebas y devolver la respuesta adecuada
            if (pruebas.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(pruebas);
            }
        }
        return ResponseEntity.badRequest().build();
    }

}
