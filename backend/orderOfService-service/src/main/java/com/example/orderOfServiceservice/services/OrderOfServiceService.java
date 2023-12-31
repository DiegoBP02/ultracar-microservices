package com.example.orderOfServiceservice.services;

import com.example.orderOfServiceservice.dtos.*;
import com.example.orderOfServiceservice.entities.OrderOfService;
import com.example.orderOfServiceservice.exceptions.ResourceNotFoundException;
import com.example.orderOfServiceservice.exceptions.UniqueConstraintViolationException;
import com.example.orderOfServiceservice.repositories.OrderOfServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

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
    private VehicleService vehicleService;

    @Autowired
    private SpecificServiceService specificServiceService;

    @Autowired
    private GeneralServiceService generalServiceService;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private ClientService clientService;

    public OrderOfServiceResponse create(OrderOfServiceDTO generalServiceDTO) {
        ClientResponse clientResponse = clientService.findClientByCpf(generalServiceDTO.getClientCpf());
        VehicleResponse vehicleResponse = vehicleService.findById(generalServiceDTO.getVehicleId());
        List<SpecificServiceResponse> specificServiceResponses =
                (generalServiceDTO.getSpecificServiceIds() != null)
                        ? specificServiceService.findAllByIdIn(generalServiceDTO.getSpecificServiceIds())
                        : Collections.emptyList();
        List<GeneralServiceResponse> generalServiceResponses =
                (generalServiceDTO.getGeneralServiceIds() != null)
                        ? generalServiceService.findAllByIdIn(generalServiceDTO.getGeneralServiceIds())
                        : Collections.emptyList();
        List<ObservationResponse> observationsResponses = (generalServiceDTO.getObservationIds() != null)
                ? observationService.findAllByIdIn(generalServiceDTO.getObservationIds())
                : Collections.emptyList();
        try {
            OrderOfService orderOfService = OrderOfService.builder()
                    .createdAt(Instant.now())
                    .diagnosticId(randomValue())
                    .clientCpf(clientResponse.getCpf())
                    .vehicleId(vehicleResponse.getId())
                    .specificServicesIds(specificServiceResponses.isEmpty() ?
                            null : specificServiceResponses.stream()
                            .map(SpecificServiceResponse::getId).toList())
                    .generalServicesIds(generalServiceResponses.isEmpty() ?
                            null : generalServiceResponses.stream()
                            .map(GeneralServiceResponse::getId).toList())
                    .observationsIds(observationsResponses.isEmpty() ?
                            null : observationsResponses.stream().map(ObservationResponse::getId).toList())
                    .build();

            orderOfServiceRepository.save(orderOfService);
            return OrderOfServiceResponse.builder()
                    .orderOfService(orderOfService)
                    .clientResponse(clientResponse)
                    .vehicleResponse(vehicleResponse)
                    .observationResponses(observationsResponses)
                    .specificServiceResponses(specificServiceResponses)
                    .generalServiceResponses(generalServiceResponses)
                    .build();
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
        ClientResponse clientResponse = clientService.findClientByCpf(orderOfService.getClientCpf());
        VehicleResponse vehicleResponse = vehicleService.findById(orderOfService.getVehicleId());
        List<ObservationResponse> observationsResponses
                = observationService.findAllByIdIn(orderOfService.getObservationsIds());
        List<SpecificServiceResponse> specificServiceResponses
                = specificServiceService.findAllByIdIn(orderOfService.getSpecificServicesIds());
        List<GeneralServiceResponse> generalServiceResponses
                = generalServiceService.findAllByIdIn(orderOfService.getGeneralServicesIds());

        return OrderOfServiceResponse.builder()
                .orderOfService(orderOfService)
                .clientResponse(clientResponse)
                .vehicleResponse(vehicleResponse)
                .observationResponses(observationsResponses)
                .specificServiceResponses(specificServiceResponses)
                .generalServiceResponses(generalServiceResponses)
                .build();
    }
}