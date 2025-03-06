package com.example.andaluciaskills.converter;

import com.example.andaluciaskills.dto.ParticipanteDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Participante;

public class ParticipanteConverter {

    // Convertir de entidad Participante a DTO
    public static ParticipanteDTO toDTO(Participante participante) {
        ParticipanteDTO participanteDTO = new ParticipanteDTO();
        participanteDTO.setIdParticipante(participante.getIdParticipante());
        participanteDTO.setNombre(participante.getNombre());
        participanteDTO.setApellidos(participante.getApellidos());
        participanteDTO.setEmail(participante.getEmail());
        participanteDTO.setCentro(participante.getCentro());

        // Asignar el código de la especialidad si existe
        if (participante.getEspecialidad() != null) {
            participanteDTO.setNombreEspecialidad(participante.getEspecialidad().getNombre());
        }

        return participanteDTO;
    }

    // Convertir de DTO a entidad Participante
    public static Participante toEntity(ParticipanteDTO participanteDTO, Especialidad especialidad) {
        Participante participante = new Participante();
        participante.setIdParticipante(participanteDTO.getIdParticipante());
        participante.setNombre(participanteDTO.getNombre());
        participante.setApellidos(participanteDTO.getApellidos());
        participante.setEmail(participanteDTO.getEmail());
        participante.setCentro(participanteDTO.getCentro());

        // Establecer la relación con Especialidad si es proporcionada
        participante.setEspecialidad(especialidad);

        return participante;
    }
}
