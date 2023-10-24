package com.example.Ultracar;

import com.example.Ultracar.entities.*;
import com.example.Ultracar.enums.Accessory;
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
    private VehicleRepository vehicleRepository;
    @Autowired
    private ObservationRepository observationRepository;
    @Autowired
    private SpecificServiceRepository specificServiceRepository;
    @Autowired
    private GeneralServiceRepository generalServiceRepository;

    @Override
    public void run(String... args) throws Exception {
        createVehicle1();
        createVehicle2();
        createVehicle3();
        createVehicle4();
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

    private void createVehicle1() {
        Vehicle vehicle1 = Vehicle.builder()
                .clientCpf("11111111111")
                .licensePlate("GHG-1234")
                .model("Ford Fiesta")
                .year("2019")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        Vehicle savedVehicle = vehicleRepository.save(vehicle1);
        System.out.println("Vehicle saved: " + savedVehicle.getId());
    }

    private void createVehicle2() {
        Vehicle vehicle2 = Vehicle.builder()
                .clientCpf("22222222222")
                .licensePlate("GHG-1235")
                .model("Volkswagen Gol")
                .year("2018")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        vehicleRepository.save(vehicle2);
    }

    private void createVehicle3() {
        Vehicle vehicle3 = Vehicle.builder()
                .clientCpf("333333333")
                .licensePlate("GHG-1236")
                .model("Ford Fiesta")
                .year("2019")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        vehicleRepository.save(vehicle3);
    }

    private void createVehicle4() {
        Vehicle vehicle4 = Vehicle.builder()
                .clientCpf("11111111111")
                .licensePlate("GHG-1237")
                .model("Volkswagen Gol")
                .year("2018")
                .accessories(List.of(Accessory.AIRBAG, Accessory.GPS))
                .build();
        vehicleRepository.save(vehicle4);
    }

}