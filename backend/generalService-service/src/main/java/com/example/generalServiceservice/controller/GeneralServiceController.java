package com.example.generalServiceservice.controller;

import com.example.generalServiceservice.dtos.GeneralServiceDTO;
import com.example.generalServiceservice.entities.GeneralService;
import com.example.generalServiceservice.services.GeneralServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/generalService")
public class GeneralServiceController {

    @Autowired
    private GeneralServiceService generalServiceService;

    @PostMapping
    public ResponseEntity<GeneralService> create
            (@Valid @RequestBody GeneralServiceDTO generalServiceDTO) {
        GeneralService generalService = generalServiceService.create(generalServiceDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(generalService.getId()).toUri();

        return ResponseEntity.created(uri).body(generalService);
    }

    @GetMapping
    public ResponseEntity<List<GeneralService>> findAll() {
        List<GeneralService> generalServices = generalServiceService.findAll();

        return ResponseEntity.ok(generalServices);
    }

    @GetMapping(value = "/ids/{ids}")
    public ResponseEntity<List<GeneralService>> findAllByIdIn(@PathVariable List<UUID> ids) {
        List<GeneralService> specificServices = generalServiceService.findAllByIdIn(ids);
        return ResponseEntity.ok().body(specificServices);
    }

}