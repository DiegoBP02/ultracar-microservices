package com.example.observationservice.repositories;

import com.example.observationservice.entities.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, UUID> {
    List<Observation> findAllByIdIn(List<UUID> ids);
}
