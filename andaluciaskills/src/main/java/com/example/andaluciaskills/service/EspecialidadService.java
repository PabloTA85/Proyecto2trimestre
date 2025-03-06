package com.example.andaluciaskills.service;

import com.example.andaluciaskills.model.Especialidad;
import com.example.andaluciaskills.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    public Optional<Especialidad> findById(Long id) {
        return especialidadRepository.findById(id);
    }

    public Especialidad save(Especialidad especialidad) {
        // Verifica si ya existe una especialidad con el mismo código
        Optional<Especialidad> existenteCodigo = especialidadRepository.findByCodigo(especialidad.getCodigo());
        if (existenteCodigo.isPresent()
                && !existenteCodigo.get().getIdEspecialidad().equals(especialidad.getIdEspecialidad())) {
            throw new RuntimeException("El código ya está en uso: " + especialidad.getCodigo());
        }

        // Verifica si ya existe una especialidad con el mismo nombre
        Optional<Especialidad> existenteNombre = especialidadRepository.findByNombre(especialidad.getNombre());
        if (existenteNombre.isPresent()
                && !existenteNombre.get().getIdEspecialidad().equals(especialidad.getIdEspecialidad())) {
            throw new RuntimeException("El nombre ya está en uso: " + especialidad.getNombre());
        }

        return especialidadRepository.save(especialidad);
    }

    public void delete(Long id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        if (!especialidad.isPresent()) {
            throw new RuntimeException("Especialidad no encontrada con ID: " + id);
        }

        especialidadRepository.deleteById(id);
    }

    public Optional<Especialidad> findByCodigo(String codigo) {
        return especialidadRepository.findAll().stream().filter(e -> e.getCodigo().equals(codigo)).findFirst();
    }

    public Optional<Especialidad> findByNombre(String nombre) {
        return especialidadRepository.findByNombre(nombre);
    }
}
