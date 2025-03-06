package com.example.andaluciaskills.service;

import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.model.Participante;
import com.example.andaluciaskills.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    public List<Participante> findAll() {
        return participanteRepository.findAll();
    }

    public Optional<Participante> findById(Long id) {
        return participanteRepository.findById(id);
    }

    public Participante save(Participante participante) {
        return participanteRepository.save(participante);
    }

    public void delete(Long id) {
        participanteRepository.deleteById(id);
    }

    public List<Participante> findByEspecialidad(Long especialidadId) {
        return participanteRepository.findByEspecialidad_IdEspecialidad(especialidadId);
    }

}
