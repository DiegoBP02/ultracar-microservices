package com.example.orderOfServiceservice.services;

import com.example.orderOfServiceservice.dtos.GeneralServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GeneralServiceService {
    private final String GENERALSERVICE_SERVICE_URL = "http://GENERALSERVICE-SERVICE/generalService";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<GeneralServiceResponse> findAllByIdIn(List<UUID> generalServiceIds) {
        String generalServiceIdsString = generalServiceIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        String url = UriComponentsBuilder.fromHttpUrl(GENERALSERVICE_SERVICE_URL + "/ids/{generalServicesIds}")
                .buildAndExpand(generalServiceIdsString)
                .toUriString();

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GeneralServiceResponse>>() {})
                .block();
    }
}
