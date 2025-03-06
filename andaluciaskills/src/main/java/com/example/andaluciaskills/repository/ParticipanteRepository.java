package com.example.andaluciaskills.repository;

import com.example.andaluciaskills.model.Participante;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    List<Participante> findByEspecialidad_IdEspecialidad(Long especialidadId);
}
