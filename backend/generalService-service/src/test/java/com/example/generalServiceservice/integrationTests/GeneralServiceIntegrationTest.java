package com.example.generalServiceservice.integrationTests;

import com.example.generalServiceservice.DataLoader;
import com.example.generalServiceservice.dtos.GeneralServiceDTO;
import com.example.generalServiceservice.entities.GeneralService;
import com.example.generalServiceservice.enums.Situation;
import com.example.generalServiceservice.repositories.GeneralServiceRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class GeneralServiceIntegrationTest {

    private static final String PATH = "/generalService";

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
    private GeneralServiceRepository generalServiceRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    private GeneralService generalService = GeneralService.builder()
            .situation(Situation.COMPLETO)
            .serviceName("serviceName")
            .build();
    private GeneralServiceDTO generalServiceDTO = GeneralServiceDTO.builder()
            .situation(Situation.COMPLETO)
            .serviceName("serviceName")
            .build();

    private void insertGeneralService() {
        generalServiceRepository.save(generalService);
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
        generalServiceRepository.deleteAll();
    }
    @Test
    void shouldCreateObservation() throws Exception {
        mockMvc.perform(mockPostRequest(generalService))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.situation").value(generalServiceDTO.getSituation().toString()))
                .andExpect(jsonPath("$.serviceName").value(generalServiceDTO.getServiceName()));

        assertEquals(1, generalServiceRepository.findAll().size());
    }

    @Test
    void shouldFindAll() throws Exception {
        insertGeneralService();

        mockMvc.perform(mockGetRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].situation").value(generalService.getSituation().toString()))
                .andExpect(jsonPath("$[0].serviceName").value(generalService.getServiceName()));
    }

    @Test
    void shouldFindAllByIdIn() throws Exception {
        insertGeneralService();

        mockMvc.perform(mockGetRequest("ids/" + generalService.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].situation").value(generalService.getSituation().toString()))
                .andExpect(jsonPath("$[0].serviceName").value(generalService.getServiceName()));
    }

}