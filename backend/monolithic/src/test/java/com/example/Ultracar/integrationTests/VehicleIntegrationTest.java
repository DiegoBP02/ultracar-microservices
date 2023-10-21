package com.example.Ultracar.integrationTests;

import com.example.Ultracar.DataLoader;
import com.example.Ultracar.dtos.VehicleDTO;
import com.example.Ultracar.entities.Client;
import com.example.Ultracar.entities.User;
import com.example.Ultracar.entities.Vehicle;
import com.example.Ultracar.enums.Accessory;
import com.example.Ultracar.enums.Role;
import com.example.Ultracar.repositories.ClientRepository;
import com.example.Ultracar.repositories.UserRepository;
import com.example.Ultracar.repositories.VehicleRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    private UserRepository userRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ClientRepository clientRepository;
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
    private Client client = Client.builder()
            .name("client")
            .email("email")
            .cpf("123")
            .phone("123")
            .address("address")
            .build();
    private Vehicle vehicle = Vehicle.builder()
            .licensePlate("123")
            .year("1234")
            .model("model")
            .accessories(Collections.singletonList(Accessory.AIRBAG))
            .client(client)
            .build();
    private VehicleDTO vehicleDTO = VehicleDTO.builder()
            .licensePlate("123")
            .year("1234")
            .model("model")
            .accessories(Collections.singletonList(Accessory.AIRBAG))
            .clientCpf(client.getCpf())
            .build();

    private User setupUser() {
        return userRepository.findByName(user.getName())
                .orElseGet(() -> userRepository.save(user));
    }

    private Client insertClient() {
        return clientRepository.findByCpf(client.getCpf())
                .orElseGet(()->clientRepository.save(client));
    }

    private void insertVehicle() {
        Client client = insertClient();
        vehicle.setClient(client);
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
        userRepository.deleteAll();
        clientRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    void shouldCreateVehicle() throws Exception {
        clientRepository.save(client);

        mockMvc.perform(mockPostRequest(vehicleDTO).with(user(setupUser())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.licensePlate").value(vehicleDTO.getLicensePlate()))
                .andExpect(jsonPath("$.year").value(vehicleDTO.getYear()))
                .andExpect(jsonPath("$.model").value(vehicleDTO.getModel()));

        assertEquals(1, vehicleRepository.findAll().size());
    }

    @Test
    void shouldFindById() throws Exception {
        insertVehicle();

        mockMvc.perform(mockGetRequest(vehicle.getId().toString()).with(user(setupUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licensePlate").value(vehicleDTO.getLicensePlate()))
                .andExpect(jsonPath("$.year").value(vehicleDTO.getYear()))
                .andExpect(jsonPath("$.model").value(vehicleDTO.getModel()));
    }

}