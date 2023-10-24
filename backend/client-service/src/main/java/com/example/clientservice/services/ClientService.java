package com.example.clientservice.services;

import com.example.clientservice.dtos.ClientDTO;
import com.example.clientservice.entities.Client;
import com.example.clientservice.exceptions.ResourceNotFoundException;
import com.example.clientservice.exceptions.UniqueConstraintViolationException;
import com.example.clientservice.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found. CPF: " + cpf));
    }

    public Client create(ClientDTO clientDTO) {
        try {
            Client client = Client.builder()
                    .name(clientDTO.getName())
                    .cpf(clientDTO.getCpf())
                    .email(clientDTO.getEmail())
                    .phone(clientDTO.getPhone())
                    .address(clientDTO.getAddress())
                    .build();
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }
}
