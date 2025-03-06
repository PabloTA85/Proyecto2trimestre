package com.example.andaluciaskills.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.andaluciaskills.dto.AuthRequest;
import com.example.andaluciaskills.dto.LoginResponseDTO;
import com.example.andaluciaskills.dto.UsuarioDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Usuario;
import com.example.andaluciaskills.repository.EspecialidadRepository;
import com.example.andaluciaskills.security.JwtService;
import com.example.andaluciaskills.service.CustomUsuarioDetailsService;
import com.example.andaluciaskills.service.EspecialidadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUsuarioDetailsService customUsuarioDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private EspecialidadService especialidadService;

    @Operation(summary = "Iniciar sesión del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            // Obtener los detalles del usuario desde el contexto de autenticación
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Obtener el usuario completo desde el servicio
            Usuario usuario = customUsuarioDetailsService.findByUsername(userDetails.getUsername());

            // Obtener la especialidad del usuario
            Especialidad especialidad = usuario.getEspecialidad();
            String especialidadNombre = especialidad != null ? especialidad.getNombre() : "No asignada";

            // Generar el token JWT utilizando el servicio
            String token = jwtService.generateToken(userDetails);

            // Crear un LoginResponseDTO con el role, username, token y especialidad
            LoginResponseDTO responseDTO = new LoginResponseDTO(
                    userDetails.getAuthorities().toString(),
                    userDetails.getUsername(),
                    token,
                    especialidadNombre,
                    usuario.getIdUser());

            // Devolver la respuesta con el DTO
            return ResponseEntity.ok(responseDTO);

        } catch (BadCredentialsException e) {
            // Respuesta para credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "El usuario ya existe")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioDTO usuarioDTO) {

        // Verificamos si el usuario ya existe
        if (customUsuarioDetailsService.existsByUsername(usuarioDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El usuario ya existe"));
        }

        // Creamos el usuario a partir del DTO
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(usuarioDTO.getNombre());
        nuevoUsuario.setApellidos(usuarioDTO.getApellidos());
        nuevoUsuario.setUsername(usuarioDTO.getUsername());
        nuevoUsuario.setPassword(usuarioDTO.getPassword());
        nuevoUsuario.setDni(usuarioDTO.getDni());
        nuevoUsuario.setRole(usuarioDTO.getRole());

        // Asignar la especialidad (si aplica)
        if (usuarioDTO.getIdEspecialidad() != null) {
            Especialidad especialidad = especialidadRepository.findById(usuarioDTO.getIdEspecialidad())
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
            nuevoUsuario.setEspecialidad(especialidad);
        }

        // Registramos al usuario
        customUsuarioDetailsService.registerUser(nuevoUsuario);

        // Devuelve una respuesta JSON
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Usuario registrado correctamente"));
    }

    // Clases de respuesta para la API (error y éxito)
    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

}
