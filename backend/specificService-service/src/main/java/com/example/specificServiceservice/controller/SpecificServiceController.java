package com.example.specificServiceservice.controller;

import com.example.specificServiceservice.dtos.SpecificServiceDTO;
import com.example.specificServiceservice.entities.SpecificService;
import com.example.specificServiceservice.services.SpecificServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @GetMapping(value = "/ids/{ids}")
    public ResponseEntity<List<SpecificService>> findAllByIdIn(@PathVariable List<UUID> ids) {
        List<SpecificService> specificServices = specificServiceService.findAllByIdIn(ids);
        return ResponseEntity.ok().body(specificServices);
    }

}