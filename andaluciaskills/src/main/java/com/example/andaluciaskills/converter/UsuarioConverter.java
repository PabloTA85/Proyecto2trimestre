package com.example.andaluciaskills.converter;

import com.example.andaluciaskills.dto.UsuarioDTO;
import com.example.andaluciaskills.dto.UsuarioResponseDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConverter {

    public Usuario dtoToEntity(UsuarioDTO usuarioDTO, Especialidad especialidad) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setDni(usuarioDTO.getDni());
        usuario.setRole(usuarioDTO.getRole());
        usuario.setEspecialidad(especialidad);
        return usuario;
    }

    public UsuarioDTO entityToDto(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellidos(usuario.getApellidos());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setDni(usuario.getDni());
        usuarioDTO.setRole(usuario.getRole());
        if (usuario.getEspecialidad() != null) {
            usuarioDTO.setIdEspecialidad(usuario.getEspecialidad().getIdEspecialidad());
        }
        return usuarioDTO;
    }
    
    // Método para convertir a UsuarioResponseDTO, devolviendo el nombre de la especialidad
    public UsuarioResponseDTO entityToResponseDto(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUser(usuario.getIdUser());  // Asegurando que se asigna el idUser
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setUsername(usuario.getUsername());
        dto.setPassword(usuario.getPassword());
        dto.setDni(usuario.getDni());
        dto.setRole(usuario.getRole());
        if (usuario.getEspecialidad() != null) {
            dto.setEspecialidad(usuario.getEspecialidad().getNombre());
        } else {
            dto.setEspecialidad(null);  // O un valor por defecto si lo prefieres
        }
        return dto;
    }
}

