package com.example.orderOfServiceservice.dtos;

import com.example.orderOfServiceservice.entities.OrderOfService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
    private List<GeneralServiceResponse> generalServiceResponses;
    private List<ObservationResponse> observationResponses;

    @Builder
    public OrderOfServiceResponse(OrderOfService orderOfService,
                                  ClientResponse clientResponse,
                                  VehicleResponse vehicleResponse,
                                  List<ObservationResponse> observationResponses,
                                  List<SpecificServiceResponse> specificServiceResponses,
                                  List<GeneralServiceResponse> generalServiceResponses) {
        this.id= orderOfService.getId();
        this.createdAt = orderOfService.getCreatedAt();
        this.diagnosticId = orderOfService.getDiagnosticId();
        this.clientResponse = clientResponse;
        this.vehicleResponse = vehicleResponse;
        this.specificServiceResponses = specificServiceResponses;
        this.generalServiceResponses = generalServiceResponses;
        this.observationResponses = observationResponses;
    }

}
