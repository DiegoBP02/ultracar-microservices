package com.example.Ultracar.integrationTests;

import com.example.Ultracar.DataLoader;
import com.example.Ultracar.dtos.ClientResponse;
import com.example.Ultracar.dtos.OrderOfServiceDTO;
import com.example.Ultracar.entities.*;
import com.example.Ultracar.enums.Accessory;
import com.example.Ultracar.enums.Situation;
import com.example.Ultracar.repositories.*;
import com.example.Ultracar.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import java.util.UUID;

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
    private VehicleRepository vehicleRepository;
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
    @MockBean
    private ClientService clientService;

    private ClientResponse clientResponse = ClientResponse.builder()
            .id(UUID.randomUUID())
            .name("client")
            .email("email")
            .cpf("123")
            .phone("11111111111")
            .address("address")
            .build();
    private Vehicle vehicle = Vehicle.builder()
            .licensePlate("123")
            .year("1234")
            .model("model")
            .accessories(Collections.singletonList(Accessory.AIRBAG))
            .clientCpf(clientResponse.getCpf())
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
            .clientId(UUID.randomUUID())
            .observations(Collections.singletonList(observation))
            .specificServices(Collections.singletonList(specificService))
            .generalServices(Collections.singletonList(generalService))
            .createdAt(Instant.now())
            .diagnosticId("12345")
            .build();

    private void insertVehicle() {
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
        vehicleRepository.deleteAll();
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

        Mockito.when(clientService.findClientByCpf(clientResponse.getCpf())).thenReturn(clientResponse);

        OrderOfServiceDTO orderOfServiceDTO = OrderOfServiceDTO.builder()
                .clientCpf(clientResponse.getCpf())
                .vehicleId(vehicle.getId())
                .observationIds(Collections.singletonList(observation.getId()))
                .specificServiceIds(Collections.singletonList(specificService.getId()))
                .generalServiceIds(Collections.singletonList(generalService.getId()))
                .build();

        mockMvc.perform(mockPostRequest(orderOfServiceDTO))
                .andExpect(status().isCreated());

        assertEquals(1, orderOfServiceRepository.findAll().size());
    }

    @Test
    void shouldFindById() throws Exception {
        insertOrderOfService();

        mockMvc.perform(mockGetRequest(orderOfService.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.diagnosticId").value(orderOfService.getDiagnosticId()))
                .andExpect(jsonPath("$.clientId").isString())
                .andExpect(jsonPath("$.vehicle.licensePlate").value(vehicle.getLicensePlate()))
                .andExpect(jsonPath("$.observations[0].name").value(observation.getName()))
                .andExpect(jsonPath("$.specificServices[0].serviceName").value(specificService.getServiceName()))
                .andExpect(jsonPath("$.generalServices[0].serviceName").value(generalService.getServiceName()));
    }

}