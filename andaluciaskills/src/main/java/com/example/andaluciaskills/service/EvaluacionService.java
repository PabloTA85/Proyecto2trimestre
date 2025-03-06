package com.example.andaluciaskills.service;

import com.example.andaluciaskills.model.Evaluacion;
import com.example.andaluciaskills.repository.EvaluacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    public List<Evaluacion> findAll() {
        return evaluacionRepository.findAll();
    }

    public Optional<Evaluacion> findById(Long id) {
        return evaluacionRepository.findById(id);
    }

    public Evaluacion save(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    public void delete(Long id) {
        evaluacionRepository.deleteById(id);
    }

    public List<Evaluacion> getEvaluacionesPorUsuario(Long userId) {
        return evaluacionRepository.findByEvaluador_IdUser(userId); // Cambiado aquí también
    }
}
