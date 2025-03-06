package com.example.andaluciaskills.controller;

import com.example.andaluciaskills.model.Item;
import com.example.andaluciaskills.model.Prueba;
import com.example.andaluciaskills.repository.PruebaRepository;
import com.example.andaluciaskills.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private PruebaRepository pruebaRepository;

    @Operation(summary = "Obtener todos los items", description = "Obtiene todos los items registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de items obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_EXPERTO')")
    public List<Item> getAllItems() {
        return itemService.findAll();
    }

    @Operation(summary = "Obtener items por especialidad", description = "Obtiene los items asociados a una especialidad.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Items obtenidos exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
    })
    @GetMapping("/especialidad/{id}")
    @PreAuthorize("hasRole('ROLE_EXPERTO')")
    public List<Item> getItemsByEspecialidad(@Parameter(description = "ID de la especialidad") @PathVariable Long id) {
        return itemService.findByEspecialidadId(id);
    }

    @Operation(summary = "Obtener un item por su ID", description = "Obtiene un item específico por su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item encontrado"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado"),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EXPERTO')")
    public ResponseEntity<Item> getItemById(@Parameter(description = "ID del item") @PathVariable Long id) {
        Optional<Item> item = itemService.findById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo item", description = "Crea nuevos items y los asocia con una prueba.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Items creados exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @PostMapping("/create/{pruebaId}")
    @PreAuthorize("hasRole('ROLE_EXPERTO')")
    public ResponseEntity<Void> createItem(@Parameter(description = "Lista de items a crear") @RequestBody List<Item> items, 
                                           @Parameter(description = "ID de la prueba asociada a los items") @PathVariable Long pruebaId) {
        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Prueba prueba = pruebaRepository.findById(pruebaId)
                    .orElseThrow(() -> new RuntimeException("Prueba no encontrada con id: " + pruebaId));

            items.forEach(item -> item.setPrueba(prueba));

            itemService.saveAll(items);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar un item", description = "Actualiza un item específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EXPERTO')")
    public ResponseEntity<Item> updateItem(@Parameter(description = "ID del item a actualizar") @PathVariable Long id, 
                                           @Parameter(description = "Datos del item a actualizar") @RequestBody Item item) {
        if (!itemService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        item.setIdItem(id);
        return ResponseEntity.ok(itemService.save(item));
    }

    @Operation(summary = "Eliminar un item", description = "Elimina un item específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EXPERTO')")
    public ResponseEntity<Void> deleteItem(@Parameter(description = "ID del item a eliminar") @PathVariable Long id) {
        if (!itemService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
