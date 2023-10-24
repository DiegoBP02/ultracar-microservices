package com.example.vehicleservice.controllers;

import com.example.vehicleservice.dtos.VehicleDTO;
import com.example.vehicleservice.entities.Vehicle;
import com.example.vehicleservice.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> create(@Valid @RequestBody VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleService.create(vehicleDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vehicle.getId()).toUri();

        return ResponseEntity.created(uri).body(vehicle);
    }

    @GetMapping(value = "/clientCpf/{clientCpf}")
    public ResponseEntity<List<Vehicle>> findAllVehicleByClientCpf
            (@PathVariable("clientCpf") String clientCpf) {
        List<Vehicle> vehicles = vehicleService.findAllByClientCpf(clientCpf);
        return ResponseEntity.ok().body(vehicles);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Vehicle> findById(@PathVariable("id") UUID id) {
        Vehicle vehicle = vehicleService.findById(id);
        return ResponseEntity.ok().body(vehicle);
    }

}