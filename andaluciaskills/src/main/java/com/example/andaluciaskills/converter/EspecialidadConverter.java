package com.example.andaluciaskills.converter;

import com.example.andaluciaskills.dto.EspecialidadDTO;
import com.example.andaluciaskills.model.Especialidad;

public class EspecialidadConverter {

    // Convertir de entidad Especialidad a EspecialidadDTO
    public static EspecialidadDTO toDTO(Especialidad especialidad) {
        EspecialidadDTO especialidadDTO = new EspecialidadDTO();
        especialidadDTO.setIdEspecialidad(especialidad.getIdEspecialidad());
        especialidadDTO.setCodigo(especialidad.getCodigo());
        especialidadDTO.setNombre(especialidad.getNombre());
        return especialidadDTO;
    }

    // Convertir de EspecialidadDTO a entidad Especialidad
    public static Especialidad toEntity(EspecialidadDTO especialidadDTO) {
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(especialidadDTO.getIdEspecialidad());
        especialidad.setCodigo(especialidadDTO.getCodigo());
        especialidad.setNombre(especialidadDTO.getNombre());
        return especialidad;
    }
}
