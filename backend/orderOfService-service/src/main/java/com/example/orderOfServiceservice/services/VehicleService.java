package com.example.orderOfServiceservice.services;

import com.example.orderOfServiceservice.dtos.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class VehicleService {
    private final String VEHICLE_SERVICE_URL = "http://VEHICLE-SERVICE/vehicle";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public VehicleResponse findById(UUID vehicleId) {
        String url = UriComponentsBuilder.fromHttpUrl(VEHICLE_SERVICE_URL + "/{vehicleId}")
                .buildAndExpand(vehicleId)
                .toUriString();

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(VehicleResponse.class)
                .block();
    }
}
