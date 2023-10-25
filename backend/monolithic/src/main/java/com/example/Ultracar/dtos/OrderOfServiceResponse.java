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
    private List<SpecificServiceResponse> specificServiceResponses;
    private List<ServiceResponse> generalServices;
    private List<ObservationResponse> observationResponses;

    @Builder
    public OrderOfServiceResponse(OrderOfService orderOfService,
                                  ClientResponse clientResponse,
                                  VehicleResponse vehicleResponse,
                                  List<ObservationResponse> observationResponses,
                                  List<SpecificServiceResponse> specificServiceResponses) {
        this.id= orderOfService.getId();
        this.createdAt = orderOfService.getCreatedAt();
        this.diagnosticId = orderOfService.getDiagnosticId();
        this.clientResponse = clientResponse;
        this.vehicleResponse = vehicleResponse;
        this.specificServiceResponses = specificServiceResponses;
        this.generalServices = orderOfService.getGeneralServices() != null
                ? mapEntitiesToServiceResponses(
                        orderOfService.getGeneralServices(),
                        GeneralService::getServiceName,
                        GeneralService::getSituation)
                : null;
        this.observationResponses = observationResponses;
    }

    public <T> List<ServiceResponse> mapEntitiesToServiceResponses
            (List<T> entities,
             Function<T, String> nameFunction,
             Function<T, Situation> situationFunction) {
        return entities.stream()
                .map(entity -> new ServiceResponse(nameFunction.apply(entity),
                        situationFunction.apply(entity))).toList();
    }

}
