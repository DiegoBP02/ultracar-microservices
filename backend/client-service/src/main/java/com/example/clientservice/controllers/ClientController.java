package com.example.clientservice.controllers;

import com.example.clientservice.dtos.ClientDTO;
import com.example.clientservice.entities.Client;
import com.example.clientservice.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

}