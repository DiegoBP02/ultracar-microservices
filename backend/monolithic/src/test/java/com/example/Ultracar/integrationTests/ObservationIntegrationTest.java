package com.example.Ultracar.integrationTests;

import com.example.Ultracar.DataLoader;
import com.example.Ultracar.dtos.ObservationDTO;
import com.example.Ultracar.entities.Observation;
import com.example.Ultracar.entities.User;
import com.example.Ultracar.enums.Role;
import com.example.Ultracar.enums.Situation;
import com.example.Ultracar.repositories.ObservationRepository;
import com.example.Ultracar.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    private UserRepository userRepository;
    @Autowired
    private ObservationRepository observationRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    private User user = User.builder()
            .name("user")
            .password("password")
            .role(Role.EMPLOYEE)
            .build();
    private Observation observation = Observation.builder()
            .situation(Situation.COMPLETO)
            .name("name")
            .build();
    private ObservationDTO observationDTO = ObservationDTO.builder()
            .situation(Situation.COMPLETO)
            .name("name")
            .build();

    private User setupUser() {
        return userRepository.findByName(user.getName())
                .orElseGet(() -> userRepository.save(user));
    }

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

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        observationRepository.deleteAll();
    }
    @Test
    void shouldCreateObservation() throws Exception {
        mockMvc.perform(mockPostRequest(observationDTO).with(user(setupUser())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.situation").value(observationDTO.getSituation().toString()))
                .andExpect(jsonPath("$.name").value(observationDTO.getName()));

        assertEquals(1, observationRepository.findAll().size());
    }

    @Test
    void shouldFindAll() throws Exception {
        insertObservation();

        mockMvc.perform(mockGetRequest().with(user(setupUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].situation").value(observationDTO.getSituation().toString()))
                .andExpect(jsonPath("$[0].name").value(observationDTO.getName()));
    }

}