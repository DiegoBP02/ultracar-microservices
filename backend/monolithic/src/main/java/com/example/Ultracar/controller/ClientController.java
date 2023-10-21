package com.example.Ultracar.controller;

import com.example.Ultracar.dtos.ClientDTO;
import com.example.Ultracar.entities.Client;
import com.example.Ultracar.entities.Vehicle;
import com.example.Ultracar.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody ClientDTO clientDTO) {
        Client client = clientService.create(clientDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId()).toUri();

        return ResponseEntity.created(uri).body(client);
    }

    @GetMapping(value = "/{cpf}")
    public ResponseEntity<Client> findByCpf(@PathVariable("cpf") String cpf) {
        Client client = clientService.findByCpf(cpf);
        return ResponseEntity.ok().body(client);
    }

    @GetMapping("/{cpf}/cars")
    public ResponseEntity<List<Vehicle>> findVehiclesByClientCpf(@PathVariable("cpf") String cpf) {
        List<Vehicle> cars = clientService.findVehiclesByClientCpf(cpf);
        return ResponseEntity.ok(cars);
    }

}