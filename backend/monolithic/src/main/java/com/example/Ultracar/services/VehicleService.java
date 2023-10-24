package com.example.Ultracar.services;

import com.example.Ultracar.dtos.VehicleDTO;
import com.example.Ultracar.dtos.VehicleResponseWithClientCpf;
import com.example.Ultracar.entities.Vehicle;
import com.example.Ultracar.exceptions.ResourceNotFoundException;
import com.example.Ultracar.exceptions.UniqueConstraintViolationException;
import com.example.Ultracar.repositories.VehicleRepository;
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

    public VehicleResponseWithClientCpf findByIdWithClientCpf(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found. ID: " + id));

        return mapVehicleToVehicleResponseWithClientCpf(vehicle);
    }

    public List<Vehicle> findAllByClientCpf(String clientCpf) {
        return vehicleRepository.findAllByClientCpf(clientCpf);
    }


    private VehicleResponseWithClientCpf mapVehicleToVehicleResponseWithClientCpf(Vehicle vehicle) {
        return VehicleResponseWithClientCpf.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .year(vehicle.getYear())
                .model(vehicle.getModel())
                .accessories(vehicle.getAccessories())
                .clientCpf(vehicle.getClientCpf())
                .build();
    }
}
