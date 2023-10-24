package com.example.Ultracar.services;

import com.example.Ultracar.dtos.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ClientService {

    private final String CLIENT_SERVICE_URL = "http://CLIENT-SERVICE/client";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public ClientResponse findClientByCpf(String clientCpf) {
        String url = UriComponentsBuilder.fromHttpUrl(CLIENT_SERVICE_URL + "/{clientCpf}")
                .buildAndExpand(clientCpf)
                .toUriString();

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(ClientResponse.class)
                .block();
    }
}
