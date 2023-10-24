package com.example.clientservice;

import com.example.clientservice.entities.Client;
import com.example.clientservice.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception {
        Client client1 = createClient1();
        Client client2 = createClient2();
        Client client3 = createClient3();
    }

    private Client createClient1() {
        Client client1 = Client.builder()
                .name("Cliente 1")
                .phone("111111111")
                .email("cliente1@email.com")
                .cpf("11111111111")
                .address("Endereço 1")
                .build();
        Client savedClient = clientRepository.save(client1);
        System.out.println("Client saved: " + savedClient.getId());
        return savedClient;
    }

    private Client createClient2() {
        Client client2 = Client.builder()
                .name("Cliente 2")
                .phone("222222222")
                .email("cliente2@email.com")
                .cpf("22222222222")
                .address("Endereço 2")
                .build();
        return clientRepository.save(client2);
    }

    private Client createClient3() {
        Client client3 = Client.builder()
                .name("Cliente 3")
                .phone("333333333")
                .email("cliente3@email.com")
                .cpf("33333333333")
                .address("Endereço 3")
                .build();
        return clientRepository.save(client3);
    }
}
