package com.example.andaluciaskills.repository;

import com.example.andaluciaskills.model.EvaluacionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionItemRepository extends JpaRepository<EvaluacionItem, Long> {
}
