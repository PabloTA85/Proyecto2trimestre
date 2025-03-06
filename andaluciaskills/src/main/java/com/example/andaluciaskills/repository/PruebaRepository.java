package com.example.andaluciaskills.repository;

import com.example.andaluciaskills.model.Prueba;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PruebaRepository extends JpaRepository<Prueba, Long> {
    
    Optional<Prueba> findByEnunciado(String enunciado);
    List<Prueba> findByEspecialidad_IdEspecialidad(Long idEspecialidad);

}
