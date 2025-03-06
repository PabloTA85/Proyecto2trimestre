package com.example.andaluciaskills.converter;

import com.example.andaluciaskills.dto.EvaluacionDTO;
import com.example.andaluciaskills.model.Evaluacion;
import com.example.andaluciaskills.model.Participante;
import com.example.andaluciaskills.model.Prueba;
import com.example.andaluciaskills.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class EvaluacionConverter {

    public EvaluacionDTO toDTO(Evaluacion evaluacion) {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setParticipanteId(evaluacion.getParticipante().getIdParticipante()); 
        dto.setPruebaId(evaluacion.getPrueba().getIdPrueba());
        dto.setEvaluadorId(evaluacion.getEvaluador().getIdUser()); 
        dto.setNotaFinal(evaluacion.getNotaFinal());
        return dto;
    }

    public Evaluacion toEntity(EvaluacionDTO dto, Participante participante, Prueba prueba, Usuario evaluador) {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setParticipante(participante);
        evaluacion.setPrueba(prueba);
        evaluacion.setEvaluador(evaluador);
        evaluacion.setNotaFinal(dto.getNotaFinal());
        return evaluacion;
    }
}
