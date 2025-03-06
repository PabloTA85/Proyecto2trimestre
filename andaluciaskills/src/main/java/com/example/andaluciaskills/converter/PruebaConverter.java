package com.example.andaluciaskills.converter;

import org.springframework.stereotype.Component;

import com.example.andaluciaskills.dto.PruebaDTO;
import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Prueba;

@Component
public class PruebaConverter {

    // Convertir de entidad Prueba a PruebaDTO
    public static PruebaDTO toDTO(Prueba prueba) {
        PruebaDTO pruebaDTO = new PruebaDTO();
        pruebaDTO.setIdPrueba(prueba.getIdPrueba());
        pruebaDTO.setEnunciado(prueba.getEnunciado());
        pruebaDTO.setPuntuacionMaxima(prueba.getPuntuacionMaxima());
        pruebaDTO.setPdfData(prueba.getPdfData());

        if (prueba.getEspecialidad() != null) {
            pruebaDTO.setNombreEspecialidad(prueba.getEspecialidad().getCodigo());
        }

        return pruebaDTO;
    }

    // Convertir de PruebaDTO a entidad Prueba
    public static Prueba toEntity(PruebaDTO pruebaDTO, Especialidad especialidad) {
        Prueba prueba = new Prueba();
        prueba.setIdPrueba(pruebaDTO.getIdPrueba());
        prueba.setEnunciado(pruebaDTO.getEnunciado());
        prueba.setPuntuacionMaxima(pruebaDTO.getPuntuacionMaxima());
        prueba.setPdfData(pruebaDTO.getPdfData());

        prueba.setEspecialidad(especialidad);

        return prueba;
    }
}
