package com.example.Ultracar.dtos;

import com.example.Ultracar.entities.*;
import com.example.Ultracar.enums.Situation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOfServiceResponse {
    private UUID id;
    private Instant createdAt;
    private String diagnosticId;
    private ClientResponse clientResponse;
    private VehicleResponse vehicleResponse;
    private List<ServiceResponse> specificServices;
    private List<ServiceResponse> generalServices;
    private List<ServiceResponse> observations;

    @Builder
    public OrderOfServiceResponse(OrderOfService orderOfService) {
        this.id= orderOfService.getId();
        this.createdAt = orderOfService.getCreatedAt();
        this.diagnosticId = orderOfService.getDiagnosticId();
        this.clientResponse = mapClientToClientResponse(orderOfService.getClient());
        this.vehicleResponse = mapVehicleToVehicleResponse(orderOfService.getVehicle());
        this.specificServices = orderOfService.getSpecificServices() != null
                ? mapEntitiesToServiceResponses(
                        orderOfService.getSpecificServices(),
                        SpecificService::getServiceName,
                        SpecificService::getSituation)
                : null;
        this.generalServices = orderOfService.getGeneralServices() != null
                ? mapEntitiesToServiceResponses(
                        orderOfService.getGeneralServices(),
                        GeneralService::getServiceName,
                        GeneralService::getSituation)
                : null;
        this.observations = orderOfService.getObservations() != null
                ? mapEntitiesToServiceResponses(
                        orderOfService.getObservations(),
                        Observation::getName,
                        Observation::getSituation)
                : null;
    }

    public <T> List<ServiceResponse> mapEntitiesToServiceResponses
            (List<T> entities,
             Function<T, String> nameFunction,
             Function<T, Situation> situationFunction) {
        return entities.stream()
                .map(entity -> new ServiceResponse(nameFunction.apply(entity),
                        situationFunction.apply(entity))).toList();
    }

    private VehicleResponse mapVehicleToVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .licensePlate(vehicle.getLicensePlate())
                .accessories(vehicle.getAccessories())
                .build();
    }

    private ClientResponse mapClientToClientResponse(Client client) {
        return ClientResponse.builder()
                .name(client.getName())
                .phone(client.getPhone())
                .email(client.getEmail())
                .cpf(client.getCpf())
                .address(client.getAddress())
                .build();
    }

}
