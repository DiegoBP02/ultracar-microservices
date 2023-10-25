package com.example.observationservice.services;

import com.example.observationservice.dtos.ObservationDTO;
import com.example.observationservice.entities.Observation;
import com.example.observationservice.exceptions.UniqueConstraintViolationException;
import com.example.observationservice.repositories.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ObservationService {

    @Autowired
    private ObservationRepository observationRepository;

    public Observation create(ObservationDTO observationDTO) {
        try {
            Observation observation = Observation.builder()
                    .name(observationDTO.getName())
                    .situation(observationDTO.getSituation())
                    .build();
            return observationRepository.save(observation);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    public List<Observation> findAll() {
        return observationRepository.findAll();
    }

    public List<Observation> findAllByIdIn(List<UUID> ids) {
        return observationRepository.findAllByIdIn(ids);
    }
}
