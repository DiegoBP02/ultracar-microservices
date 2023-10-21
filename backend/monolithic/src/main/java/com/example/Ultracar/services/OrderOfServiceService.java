package com.example.Ultracar.services;

import com.example.Ultracar.dtos.OrderOfServiceDTO;
import com.example.Ultracar.dtos.OrderOfServiceResponse;
import com.example.Ultracar.entities.*;
import com.example.Ultracar.exceptions.ResourceNotFoundException;
import com.example.Ultracar.exceptions.UniqueConstraintViolationException;
import com.example.Ultracar.repositories.OrderOfServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OrderOfServiceService {

    @Autowired
    private OrderOfServiceRepository orderOfServiceRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private SpecificServiceService specificServiceService;

    @Autowired
    private GeneralServiceService generalServiceService;

    @Autowired
    private ObservationService observationService;

    public OrderOfServiceResponse create(OrderOfServiceDTO generalServiceDTO) {
        Client client = clientService.findByCpf(generalServiceDTO.getClientCpf());
        Vehicle vehicle = vehicleService.findById(generalServiceDTO.getVehicleId());
        List<SpecificService> specificServices = (generalServiceDTO.getSpecificServiceIds() != null)
                ? specificServiceService.findAllByIdIn(generalServiceDTO.getSpecificServiceIds())
                : Collections.emptyList();
        List<GeneralService> generalServices = (generalServiceDTO.getGeneralServiceIds() != null)
                ? generalServiceService.findAllByIdIn(generalServiceDTO.getGeneralServiceIds())
                : Collections.emptyList();
        List<Observation> observations = (generalServiceDTO.getObservationIds() != null)
                ? observationService.findAllByIdIn(generalServiceDTO.getObservationIds())
                : Collections.emptyList();
        try {
            OrderOfService orderOfService = OrderOfService.builder()
                    .createdAt(Instant.now())
                    .diagnosticId(randomValue())
                    .client(client)
                    .vehicle(vehicle)
                    .specificServices(specificServices.isEmpty() ? null : specificServices)
                    .generalServices(generalServices.isEmpty() ? null : generalServices)
                    .observations(observations.isEmpty() ? null : observations)
                    .build();

            orderOfServiceRepository.save(orderOfService);
            return new OrderOfServiceResponse(orderOfService);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    private String randomValue() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        return String.format("%05d", num);
    }

    public OrderOfServiceResponse findById(UUID id) {
        OrderOfService orderOfService = orderOfServiceRepository.findById(id)
                .orElseThrow(() -> new
                        ResourceNotFoundException("OrderOfService not found. ID: " + id));
        return new OrderOfServiceResponse(orderOfService);
    }
}
