package com.example.Ultracar;

import com.example.Ultracar.entities.*;
import com.example.Ultracar.enums.Situation;
import com.example.Ultracar.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ObservationRepository observationRepository;
    @Autowired
    private SpecificServiceRepository specificServiceRepository;
    @Autowired
    private GeneralServiceRepository generalServiceRepository;

    @Override
    public void run(String... args) throws Exception {
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

}