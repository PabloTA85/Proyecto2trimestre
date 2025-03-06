package com.example.andaluciaskills.repository;

import com.example.andaluciaskills.model.Item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

     @Query(value = "SELECT * FROM item i " +
                   "INNER JOIN prueba p ON i.prueba_id = p.id_prueba " +
                   "WHERE p.especialidad_id = :especialidadId", 
           nativeQuery = true)
    List<Item> findByEspecialidadId(@Param("especialidadId") Long especialidadId);
}
