package com.example.andaluciaskills.repository;

import com.example.andaluciaskills.model.Evaluacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByEvaluador_IdUser(Long userId);
}
