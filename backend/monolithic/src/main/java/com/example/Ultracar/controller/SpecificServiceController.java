package com.example.Ultracar.controller;

import com.example.Ultracar.dtos.SpecificServiceDTO;
import com.example.Ultracar.entities.SpecificService;
import com.example.Ultracar.services.SpecificServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/specificService")
public class SpecificServiceController {

    @Autowired
    private SpecificServiceService specificServiceService;

    @PostMapping
    public ResponseEntity<SpecificService> create
            (@Valid @RequestBody SpecificServiceDTO specificServiceDTO) {
        SpecificService specificService = specificServiceService.create(specificServiceDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(specificService.getId()).toUri();

        return ResponseEntity.created(uri).body(specificService);
    }

    @GetMapping("/{vehicleModel}")
    public ResponseEntity<List<SpecificService>> findAllSpecificServiceByVehicleModel
            (@PathVariable("vehicleModel") String vehicleModel) {
        List<SpecificService> specificServices
                = specificServiceService.findAllSpecificServiceByVehicleModel(vehicleModel);

        return ResponseEntity.ok(specificServices);
    }

}