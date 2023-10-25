package com.example.specificServiceservice;

import com.example.specificServiceservice.entities.SpecificService;
import com.example.specificServiceservice.enums.Situation;
import com.example.specificServiceservice.repositories.SpecificServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SpecificServiceRepository specificServiceRepository;

    @Override
    public void run(String... args) throws Exception {
        createSpecificServices();
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

}