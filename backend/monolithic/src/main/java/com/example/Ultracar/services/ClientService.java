package com.example.Ultracar.services;

import com.example.Ultracar.dtos.ClientDTO;
import com.example.Ultracar.entities.Client;
import com.example.Ultracar.entities.Vehicle;
import com.example.Ultracar.exceptions.ResourceNotFoundException;
import com.example.Ultracar.exceptions.UniqueConstraintViolationException;
import com.example.Ultracar.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found. CPF: " + cpf));
    }

    public List<Vehicle> findVehiclesByClientCpf(String cpf) {
        return findByCpf(cpf).getVehicles();
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
