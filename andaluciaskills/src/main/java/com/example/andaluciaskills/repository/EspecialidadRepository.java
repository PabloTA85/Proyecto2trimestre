package com.example.andaluciaskills.repository;

import com.example.andaluciaskills.model.Especialidad;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    Optional<Especialidad> findByCodigo(String codigo);
    Optional<Especialidad> findByNombre(String nombre);
}
