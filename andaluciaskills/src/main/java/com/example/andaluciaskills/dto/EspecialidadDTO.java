package com.example.andaluciaskills.dto;

import java.util.Optional;

import com.example.andaluciaskills.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadDTO {
    private Long idEspecialidad;
    private String codigo;
    private String nombre;
    public static Optional<Usuario> findById(Long idEspecialidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
}