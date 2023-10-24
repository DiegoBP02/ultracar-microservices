package com.example.vehicleservice.services;

import com.example.vehicleservice.dtos.VehicleDTO;
import com.example.vehicleservice.entities.Vehicle;
import com.example.vehicleservice.exceptions.ResourceNotFoundException;
import com.example.vehicleservice.exceptions.UniqueConstraintViolationException;
import com.example.vehicleservice.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle create(VehicleDTO vehicleDTO) {
        try {
            Vehicle vehicle = Vehicle.builder()
                    .licensePlate(vehicleDTO.getLicensePlate())
                    .year(vehicleDTO.getYear())
                    .model(vehicleDTO.getModel())
                    .accessories(vehicleDTO.getAccessories())
                    .clientCpf(vehicleDTO.getClientCpf())
                    .build();
            return vehicleRepository.save(vehicle);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    public Vehicle findById(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found. ID: " + id));
    }

    public List<Vehicle> findAllByClientCpf(String clientCpf) {
        return vehicleRepository.findAllByClientCpf(clientCpf);
    }

}
