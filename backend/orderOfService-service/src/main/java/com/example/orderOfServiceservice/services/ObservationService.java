package com.example.orderOfServiceservice.services;

import com.example.orderOfServiceservice.dtos.ObservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ObservationService {
    private final String OBSERVATION_SERVICE_URL = "http://OBSERVATION-SERVICE/observation";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<ObservationResponse> findAllByIdIn(List<UUID> observationIds) {
        String observationIdsString = observationIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        String url = UriComponentsBuilder.fromHttpUrl(OBSERVATION_SERVICE_URL + "/{observationsIds}")
                .buildAndExpand(observationIdsString)
                .toUriString();

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ObservationResponse>>() {})
                .block();
    }
}
