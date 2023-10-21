package com.example.Ultracar.controller;

import com.example.Ultracar.dtos.ObservationDTO;
import com.example.Ultracar.entities.Observation;
import com.example.Ultracar.services.ObservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/observation")
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    @PostMapping
    public ResponseEntity<Observation> create(@Valid @RequestBody ObservationDTO observationDTO) {
        Observation observation = observationService.create(observationDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(observation.getId()).toUri();

        return ResponseEntity.created(uri).body(observation);
    }

    @GetMapping
    public ResponseEntity<List<Observation>> findAll() {
        List<Observation> observations = observationService.findAll();
        return ResponseEntity.ok().body(observations);
    }

}