package com.example.Ultracar.controller;

import com.example.Ultracar.dtos.GeneralServiceDTO;
import com.example.Ultracar.entities.GeneralService;
import com.example.Ultracar.services.GeneralServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

}