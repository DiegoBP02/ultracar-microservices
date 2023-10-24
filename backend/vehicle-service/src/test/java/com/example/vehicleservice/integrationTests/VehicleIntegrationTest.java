package com.example.vehicleservice.integrationTests;

import com.example.vehicleservice.DataLoader;
import com.example.vehicleservice.dtos.VehicleDTO;
import com.example.vehicleservice.entities.Vehicle;
import com.example.vehicleservice.enums.Accessory;
import com.example.vehicleservice.repositories.VehicleRepository;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class VehicleIntegrationTest {

    private static final String PATH = "/vehicle";

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
    private VehicleRepository vehicleRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    private String clientCpf = "11111111111";
    private Vehicle vehicle = Vehicle.builder()
            .licensePlate("123")
            .year("1234")
            .model("model")
            .accessories(Collections.singletonList(Accessory.AIRBAG))
            .clientCpf(clientCpf)
            .build();
    private VehicleDTO vehicleDTO = VehicleDTO.builder()
            .licensePlate("123")
            .year("1234")
            .model("model")
            .accessories(Collections.singletonList(Accessory.AIRBAG))
            .clientCpf(clientCpf)
            .build();

    private void insertVehicle() {
        vehicleRepository.save(vehicle);
    }

    private MockHttpServletRequestBuilder mockPostRequest
            (Object requestObject) throws Exception {
        return MockMvcRequestBuilders
                .post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(requestObject));
    }

    private MockHttpServletRequestBuilder mockGetRequest(String endpoint) {
        return MockMvcRequestBuilders.get(PATH + "/" + endpoint);
    }

    @BeforeEach
    void beforeEach() {
        vehicleRepository.deleteAll();
    }

    @Test
    void shouldCreateVehicle() throws Exception {
        mockMvc.perform(mockPostRequest(vehicleDTO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.licensePlate").value(vehicleDTO.getLicensePlate()))
                .andExpect(jsonPath("$.year").value(vehicleDTO.getYear()))
                .andExpect(jsonPath("$.model").value(vehicleDTO.getModel()))
                .andExpect(jsonPath("$.clientCpf").value(clientCpf));

        assertEquals(1, vehicleRepository.findAll().size());
    }

    @Test
    void shouldFindAllByClientCpf() throws Exception {
        insertVehicle();

        mockMvc.perform(mockGetRequest("clientCpf/" + clientCpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licensePlate").value(vehicleDTO.getLicensePlate()))
                .andExpect(jsonPath("$[0].year").value(vehicleDTO.getYear()))
                .andExpect(jsonPath("$[0].model").value(vehicleDTO.getModel()))
                .andExpect(jsonPath("$[0].clientCpf").value(clientCpf));
    }

    @Test
    void shouldFindById() throws Exception {
        insertVehicle();

        mockMvc.perform(mockGetRequest(vehicle.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licensePlate").value(vehicleDTO.getLicensePlate()))
                .andExpect(jsonPath("$.year").value(vehicleDTO.getYear()))
                .andExpect(jsonPath("$.model").value(vehicleDTO.getModel()))
                .andExpect(jsonPath("$.clientCpf").value(clientCpf));
    }

}