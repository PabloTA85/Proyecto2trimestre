package com.example.andaluciaskills.controller;

import com.example.andaluciaskills.converter.UsuarioConverter;
import com.example.andaluciaskills.dto.UsuarioDTO;
import com.example.andaluciaskills.dto.UsuarioResponseDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Usuario;
import com.example.andaluciaskills.service.EspecialidadService;
import com.example.andaluciaskills.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private UsuarioConverter usuarioConverter;

    // Obtener todos los usuarios
    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioResponseDTO> usuarioResponseDTOs = usuarios.stream().map(usuario -> {
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setIdUser(usuario.getIdUser());
            responseDTO.setNombre(usuario.getNombre());
            responseDTO.setApellidos(usuario.getApellidos());
            responseDTO.setUsername(usuario.getUsername());
            responseDTO.setDni(usuario.getDni());
            responseDTO.setRole(usuario.getRole());
            // Aquí puedes obtener la especialidad desde algún otro servicio o entidad si es
            // necesario
            if (usuario.getEspecialidad() != null) {
                responseDTO.setEspecialidad(usuario.getEspecialidad().getNombre());
            }

            return responseDTO;
        }).toList();

        return ResponseEntity.ok(usuarioResponseDTOs);
    }

    // Obtener un usuario por su id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setNombre(usuario.get().getNombre());
            responseDTO.setApellidos(usuario.get().getApellidos());
            responseDTO.setUsername(usuario.get().getUsername());
            responseDTO.setDni(usuario.get().getDni());
            responseDTO.setRole(usuario.get().getRole());
            if (usuario.get().getEspecialidad() != null) {
                responseDTO.setEspecialidad(usuario.get().getEspecialidad().getNombre());
            }
            // Aquí puedes obtener la especialidad desde algún otro servicio o entidad si es
            // necesario
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setDni(usuarioDTO.getDni());
        usuario.setRole(usuarioDTO.getRole());
        // Aquí puedes asociar la especialidad si es necesario
        Usuario savedUsuario = usuarioService.save(usuario);

        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setNombre(savedUsuario.getNombre());
        responseDTO.setApellidos(savedUsuario.getApellidos());
        responseDTO.setUsername(savedUsuario.getUsername());
        responseDTO.setDni(savedUsuario.getDni());
        responseDTO.setRole(savedUsuario.getRole());
        // Aquí puedes obtener la especialidad desde algún otro servicio o entidad si es
        // necesario

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // @PutMapping("/{username}")
    // public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable String username,
    //         @RequestBody UsuarioDTO usuarioDTO) {
    //     Optional<Usuario> usuarioOptional = usuarioService.findByUsername(username);
    //     Optional<Especialidad> especialidadOptional = especialidadService.findById(usuarioDTO.getIdEspecialidad());
    //     // Verificamos si el usuario existe
    //     if (usuarioOptional.isPresent()) {
    //         Usuario usuario = usuarioOptional.get();
    //         Especialidad especialidad = especialidadOptional.get();

    //         // Actualizamos los campos del usuario
    //         usuario.setNombre(usuarioDTO.getNombre());
    //         usuario.setApellidos(usuarioDTO.getApellidos());
    //         usuario.setUsername(usuarioDTO.getUsername());
    //         usuario.setDni(usuarioDTO.getDni());
    //         usuario.setEspecialidad(especialidad);

    //         // Guardamos el usuario actualizado
    //         Usuario updatedUsuario = usuarioService.save(usuario);

    //         // Convertimos el usuario actualizado a DTO
    //         UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
    //         responseDTO.setNombre(updatedUsuario.getNombre());
    //         responseDTO.setApellidos(updatedUsuario.getApellidos());
    //         responseDTO.setUsername(updatedUsuario.getUsername());
    //         responseDTO.setDni(updatedUsuario.getDni());
    //         responseDTO.setRole(updatedUsuario.getRole());

    //         // Aquí puedes incluir la especialidad si es necesario
    //         if (updatedUsuario.getEspecialidad() != null) {
    //             responseDTO.setEspecialidad(updatedUsuario.getEspecialidad().getNombre());
    //         }

    //         return ResponseEntity.ok(responseDTO);
    //     } else {
    //         // Si no se encuentra el usuario, respondemos con un 404
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //     }
    // }

    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        // Buscar usuario por id en lugar de username
        Optional<Usuario> usuarioOptional = usuarioService.findById(id); // Usamos findById para buscar por id
        Optional<Especialidad> especialidadOptional = especialidadService.findById(usuarioDTO.getIdEspecialidad());

        // Verificamos si el usuario existe
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            Especialidad especialidad = especialidadOptional.get();

            // Actualizamos los campos del usuario
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellidos(usuarioDTO.getApellidos());
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setDni(usuarioDTO.getDni());
            usuario.setEspecialidad(especialidad);

            // Guardamos el usuario actualizado
            Usuario updatedUsuario = usuarioService.save(usuario);

            // Convertimos el usuario actualizado a DTO
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setNombre(updatedUsuario.getNombre());
            responseDTO.setApellidos(updatedUsuario.getApellidos());
            responseDTO.setUsername(updatedUsuario.getUsername());
            responseDTO.setDni(updatedUsuario.getDni());
            responseDTO.setRole(updatedUsuario.getRole());

            // Aquí puedes incluir la especialidad si es necesario
            if (updatedUsuario.getEspecialidad() != null) {
                responseDTO.setEspecialidad(updatedUsuario.getEspecialidad().getNombre());
            }

            return ResponseEntity.ok(responseDTO);
        } else {
            // Si no se encuentra el usuario, respondemos con un 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/expertos")
    public List<UsuarioResponseDTO> getExpertos() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Usuario> expertos = usuarios.stream()
                .filter(u -> "EXPERTO".equalsIgnoreCase(u.getRole()))
                .collect(Collectors.toList());
        return expertos.stream()
                .map(usuarioConverter::entityToResponseDto)
                .collect(Collectors.toList());
    }

}
