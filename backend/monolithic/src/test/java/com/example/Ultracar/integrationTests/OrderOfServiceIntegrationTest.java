package com.example.Ultracar.integrationTests;

import com.example.Ultracar.DataLoader;
import com.example.Ultracar.dtos.OrderOfServiceDTO;
import com.example.Ultracar.entities.*;
import com.example.Ultracar.enums.Accessory;
import com.example.Ultracar.enums.Role;
import com.example.Ultracar.enums.Situation;
import com.example.Ultracar.repositories.*;
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

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderOfServiceIntegrationTest {

    private static final String PATH = "/orderOfService";

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
    private ObservationRepository observationRepository;
    @Autowired
    private SpecificServiceRepository specificServiceRepository;
    @Autowired
    private GeneralServiceRepository generalServiceRepository;
    @Autowired
    private OrderOfServiceRepository orderOfServiceRepository;
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
    private Observation observation = Observation.builder()
            .situation(Situation.COMPLETO)
            .name("name")
            .build();
    private SpecificService specificService = SpecificService.builder()
            .situation(Situation.COMPLETO)
            .serviceName("serviceName")
            .vehicleModel("model")
            .build();
    private GeneralService generalService = GeneralService.builder()
            .situation(Situation.COMPLETO)
            .serviceName("serviceName")
            .build();
    private OrderOfService orderOfService = OrderOfService.builder()
            .vehicle(vehicle)
            .client(client)
            .observations(Collections.singletonList(observation))
            .specificServices(Collections.singletonList(specificService))
            .generalServices(Collections.singletonList(generalService))
            .createdAt(Instant.now())
            .diagnosticId("12345")
            .build();

    private User setupUser() {
        return userRepository.findByName(user.getName())
                .orElseGet(() -> userRepository.save(user));
    }

    private void insertClient() {
        clientRepository.save(client);
    }

    private void insertVehicle() {
        insertClient();
        vehicleRepository.save(vehicle);
    }

    private void insertObservation() {
        observationRepository.save(observation);
    }

    private void insertSpecificService() {
        specificServiceRepository.save(specificService);
    }

    private void insertGeneralService() {
        generalServiceRepository.save(generalService);
    }

    private void insertOrderOfService(){
        insertVehicle();
        insertObservation();
        insertSpecificService();
        insertGeneralService();
        orderOfServiceRepository.save(orderOfService);
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
        orderOfServiceRepository.deleteAll();
        userRepository.deleteAll();
        vehicleRepository.deleteAll();
        clientRepository.deleteAll();
        observationRepository.deleteAll();
        specificServiceRepository.deleteAll();
        generalServiceRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderOfService() throws Exception {
        insertVehicle();
        insertObservation();
        insertSpecificService();
        insertGeneralService();
        OrderOfServiceDTO orderOfServiceDTO = OrderOfServiceDTO.builder()
                .clientCpf(client.getCpf())
                .vehicleId(vehicle.getId())
                .observationIds(Collections.singletonList(observation.getId()))
                .specificServiceIds(Collections.singletonList(specificService.getId()))
                .generalServiceIds(Collections.singletonList(generalService.getId()))
                .build();

        mockMvc.perform(mockPostRequest(orderOfServiceDTO).with(user(setupUser())))
                .andExpect(status().isCreated());

        assertEquals(1, orderOfServiceRepository.findAll().size());
    }

    @Test
    void shouldFindById() throws Exception {
        insertOrderOfService();

        mockMvc.perform(mockGetRequest(orderOfService.getId().toString()).with(user(setupUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.diagnosticId").value(orderOfService.getDiagnosticId()))
                .andExpect(jsonPath("$.clientResponse.cpf").value(client.getCpf()))
                .andExpect(jsonPath("$.vehicleResponse.licensePlate").value(vehicle.getLicensePlate()))
                .andExpect(jsonPath("$.observations[0].name").value(observation.getName()))
                .andExpect(jsonPath("$.specificServices[0].name").value(specificService.getServiceName()))
                .andExpect(jsonPath("$.generalServices[0].name").value(generalService.getServiceName()));
    }

}