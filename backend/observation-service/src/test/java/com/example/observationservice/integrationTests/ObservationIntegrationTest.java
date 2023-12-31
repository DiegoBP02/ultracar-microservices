package com.example.observationservice.integrationTests;

import com.example.observationservice.DataLoader;
import com.example.observationservice.dtos.ObservationDTO;
import com.example.observationservice.entities.Observation;
import com.example.observationservice.enums.Situation;
import com.example.observationservice.repositories.ObservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ObservationIntegrationTest {

    private static final String PATH = "/observation";

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    static {
        postgreSQLContainer.start();
    }

    @Autowired
    private ObservationRepository observationRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    private Observation observation = Observation.builder()
            .situation(Situation.COMPLETO)
            .name("name")
            .build();
    private ObservationDTO observationDTO = ObservationDTO.builder()
            .situation(Situation.COMPLETO)
            .name("name")
            .build();

    private void insertObservation() {
        observationRepository.save(observation);
    }

    private MockHttpServletRequestBuilder mockPostRequest
            (Object requestObject) throws Exception {
        return MockMvcRequestBuilders
                .post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(requestObject));
    }

    private MockHttpServletRequestBuilder mockGetRequest() {
        return MockMvcRequestBuilders.get(PATH);
    }

    private MockHttpServletRequestBuilder mockGetRequest(String endpoint) {
        return MockMvcRequestBuilders.get(PATH + "/" + endpoint);
    }

    @BeforeEach
    void beforeEach() {
        observationRepository.deleteAll();
    }
    @Test
    void shouldCreateObservation() throws Exception {
        mockMvc.perform(mockPostRequest(observationDTO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.situation").value(observationDTO.getSituation().toString()))
                .andExpect(jsonPath("$.name").value(observationDTO.getName()));

        assertEquals
                (1, observationRepository.findAll().size());
    }

    @Test
    void shouldFindAll() throws Exception {
        insertObservation();

        mockMvc.perform(mockGetRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].situation").value(observationDTO.getSituation().toString()))
                .andExpect(jsonPath("$[0].name").value(observationDTO.getName()));
    }


    @Test
    void shouldFindAllByIdIn() throws Exception {
        insertObservation();

        mockMvc.perform(mockGetRequest(observation.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].situation").value(observationDTO.getSituation().toString()))
                .andExpect(jsonPath("$[0].name").value(observationDTO.getName()));
    }

}