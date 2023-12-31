package com.example.generalServiceservice;

import com.example.generalServiceservice.entities.GeneralService;
import com.example.generalServiceservice.enums.Situation;
import com.example.generalServiceservice.repositories.GeneralServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private GeneralServiceRepository generalServiceRepository;

    @Override
    public void run(String... args) throws Exception {
        createGeneralServices();
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