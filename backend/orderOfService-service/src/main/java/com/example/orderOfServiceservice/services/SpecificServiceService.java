package com.example.orderOfServiceservice.services;

import com.example.orderOfServiceservice.dtos.SpecificServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SpecificServiceService {
    private final String SPECIFICSERVICE_SERVICE_URL = "http://SPECIFICSERVICE-SERVICE/specificService";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<SpecificServiceResponse> findAllByIdIn(List<UUID> specificServiceIds) {
        String specificServiceIdsString = specificServiceIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        String url = UriComponentsBuilder.fromHttpUrl(SPECIFICSERVICE_SERVICE_URL + "/ids/{specificServicesIds}")
                .buildAndExpand(specificServiceIdsString)
                .toUriString();

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SpecificServiceResponse>>() {})
                .block();
    }
}