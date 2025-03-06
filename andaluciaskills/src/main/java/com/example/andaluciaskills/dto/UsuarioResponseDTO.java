package com.example.andaluciaskills.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long idUser;
    private String nombre;
    private String apellidos;
    private String username;
    private String password;
    private String dni;
    private String role;
    private String especialidad; // Aquí se almacena el nombre de la especialidad
}
