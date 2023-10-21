package com.example.Ultracar;

import com.example.Ultracar.entities.*;
import com.example.Ultracar.enums.Accessory;
import com.example.Ultracar.enums.Role;
import com.example.Ultracar.enums.Situation;
import com.example.Ultracar.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ObservationRepository observationRepository;
    @Autowired
    private SpecificServiceRepository specificServiceRepository;
    @Autowired
    private GeneralServiceRepository generalServiceRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdmin();
        Client client1 = createClient1();
        Client client2 = createClient2();
        Client client3 = createClient3();
        createVehicle1(client1);
        createVehicle2(client2);
        createVehicle3(client3);
        createVehicle4(client1);
        createObservations();
        createSpecificServices();
        createGeneralServices();
    }

    private void createObservations() {
        Observation observation1 = Observation.builder()
                .name("Arranhão pequeno")
                .situation(Situation.REPARAR)
                .build();

        Observation observation2 = Observation.builder()
                .name("Amassado na porta")
                .situation(Situation.COMPLETO)
                .build();

        Observation observation3 = Observation.builder()
                .name("Pintura descascada")
                .situation(Situation.EM_PROGRESSO)
                .build();

        List<Observation> observations = Arrays.asList(observation1, observation2, observation3);

        observationRepository.saveAll(observations);

        System.out.println("Observation saved: " + observation1.getId());
    }

    private void createSpecificServices() {
        SpecificService specificService1 = SpecificService.builder()
                .vehicleModel("Dodge Challenger")
                .serviceName("Troca de airbag")
                .situation(Situation.REPARAR)
                .build();
        SpecificService specificService2 = SpecificService.builder()
                .vehicleModel("Ford Fiesta")
                .serviceName("Reparo do motor")
                .situation(Situation.COMPLETO)
                .build();
        SpecificService specificService3 = SpecificService.builder()
                .vehicleModel("Volkswagen Gol")
                .serviceName("Substituição da bateria")
                .situation(Situation.EM_PROGRESSO)
                .build();

        List<SpecificService> specificServices = Arrays.asList(specificService1, specificService2,
                specificService3);

        specificServiceRepository.saveAll(specificServices);

        System.out.println("SpecificService saved: " + specificService1.getId());
    }

    private void createGeneralServices() {
        GeneralService generalService1 = GeneralService.builder()
                .serviceName("Lavagem do veículo")
                .situation(Situation.PENDENTE)
                .build();

        GeneralService generalService2 = GeneralService.builder()
                .serviceName("Troca de óleo")
                .situation(Situation.EM_PROGRESSO)
                .build();

        GeneralService generalService3 = GeneralService.builder()
                .serviceName("Alinhamento e balanceamento")
                .situation(Situation.EM_PROGRESSO)
                .build();

        List<GeneralService> generalServices = Arrays.asList(generalService1, generalService2, generalService3);

        generalServiceRepository.saveAll(generalServices);

        System.out.println("GeneralService saved: " + generalService1.getId());
    }

    private void createVehicle1(Client client1) {
        Vehicle vehicle1 = Vehicle.builder()
                .client(client1)
                .licensePlate("GHG-1234")
                .model("Ford Fiesta")
                .year("2019")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        Vehicle savedVehicle = vehicleRepository.save(vehicle1);
        System.out.println("Vehicle saved: " + savedVehicle.getId());
    }

    private void createVehicle2(Client client2) {
        Vehicle vehicle2 = Vehicle.builder()
                .client(client2)
                .licensePlate("GHG-1235")
                .model("Volkswagen Gol")
                .year("2018")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        vehicleRepository.save(vehicle2);
    }

    private void createVehicle3(Client client3) {
        Vehicle vehicle3 = Vehicle.builder()
                .client(client3)
                .licensePlate("GHG-1236")
                .model("Ford Fiesta")
                .year("2019")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        vehicleRepository.save(vehicle3);
    }

    private void createVehicle4(Client client1) {
        Vehicle vehicle4 = Vehicle.builder()
                .client(client1)
                .licensePlate("GHG-1237")
                .model("Volkswagen Gol")
                .year("2018")
                .accessories(List.of(Accessory.AIRBAG, Accessory.GPS))
                .build();
        vehicleRepository.save(vehicle4);
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

    private void createAdmin() {
        User admin = User.builder()
                .name("admin")
                .password(passwordEncoder.encode("senha"))
                .role(Role.ADMIN)
                .build();
        User savedAdmin = userRepository.save(admin);
        System.out.println("Admin saved: " + savedAdmin.getId());
    }
}