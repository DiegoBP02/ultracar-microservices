package com.example.observationservice;

import com.example.observationservice.entities.Observation;
import com.example.observationservice.enums.Situation;
import com.example.observationservice.repositories.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ObservationRepository observationRepository;

    @Override
    public void run(String... args) throws Exception {
        createObservations();
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

}
