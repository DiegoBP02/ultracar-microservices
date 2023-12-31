package com.example.orderOfServiceservice.integrationTests;


import com.example.orderOfServiceservice.dtos.*;
import com.example.orderOfServiceservice.entities.OrderOfService;
import com.example.orderOfServiceservice.enums.Situation;
import com.example.orderOfServiceservice.repositories.OrderOfServiceRepository;
import com.example.orderOfServiceservice.services.*;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
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
    private OrderOfServiceRepository orderOfServiceRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private ClientService clientService;
    @MockBean
    private VehicleService vehicleService;
    @MockBean
    private ObservationService observationService;
    @MockBean
    private SpecificServiceService specificServiceService;
    @MockBean
    private GeneralServiceService generalServiceService;

    private ClientResponse clientResponse = ClientResponse.builder()
            .id(UUID.randomUUID())
            .name("client")
            .email("email")
            .cpf("123")
            .phone("11111111111")
            .address("address")
            .build();
    private VehicleResponse vehicleResponse = VehicleResponse.builder()
            .id(UUID.randomUUID())
            .licensePlate("123")
            .year("1234")
            .model("model")
            .accessories(Collections.singletonList(VehicleResponse.Accessory.AIRBAG))
            .clientCpf(clientResponse.getCpf())
            .build();
    private ObservationResponse observationResponse = ObservationResponse.builder()
            .situation(Situation.COMPLETO)
            .name("name")
            .id(UUID.randomUUID())
            .build();
    private SpecificServiceResponse specificServiceResponse = SpecificServiceResponse.builder()
            .id(UUID.randomUUID())
            .situation(Situation.COMPLETO)
            .serviceName("serviceName")
            .vehicleModel("model")
            .build();
    private GeneralServiceResponse generalServiceResponse = GeneralServiceResponse.builder()
            .id(UUID.randomUUID())
            .situation(Situation.COMPLETO)
            .serviceName("serviceName")
            .build();
    private OrderOfService orderOfService = OrderOfService.builder()
            .vehicleId(vehicleResponse.getId())
            .clientCpf(clientResponse.getCpf())
            .observationsIds(Collections.singletonList(observationResponse.getId()))
            .specificServicesIds(Collections.singletonList(specificServiceResponse.getId()))
            .generalServicesIds(Collections.singletonList(generalServiceResponse.getId()))
            .createdAt(Instant.now())
            .diagnosticId("12345")
            .build();

    private void insertOrderOfService(){
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
    }

    @Test
    void shouldCreateOrderOfService() throws Exception {
        when(clientService.findClientByCpf(clientResponse.getCpf())).thenReturn(clientResponse);
        when(vehicleService.findById(vehicleResponse.getId())).thenReturn(vehicleResponse);
        when(observationService.findAllByIdIn(Collections.singletonList(orderOfService.getId())))
                .thenReturn(Collections.singletonList(observationResponse));
        when(specificServiceService.findAllByIdIn(Collections.singletonList(specificServiceResponse.getId())))
                .thenReturn(Collections.singletonList(specificServiceResponse));
        when(generalServiceService.findAllByIdIn(Collections.singletonList(generalServiceResponse.getId())))
                .thenReturn(Collections.singletonList(generalServiceResponse));

        OrderOfServiceDTO orderOfServiceDTO = OrderOfServiceDTO.builder()
                .clientCpf(clientResponse.getCpf())
                .vehicleId(vehicleResponse.getId())
                .observationIds(Collections.singletonList(observationResponse.getId()))
                .specificServiceIds(Collections.singletonList(specificServiceResponse.getId()))
                .generalServiceIds(Collections.singletonList(generalServiceResponse.getId()))
                .build();

        mockMvc.perform(mockPostRequest(orderOfServiceDTO))
                .andExpect(status().isCreated());

        assertEquals(1, orderOfServiceRepository.findAll().size());
    }

    @Test
    void shouldFindById() throws Exception {
        insertOrderOfService();

        when(clientService.findClientByCpf(clientResponse.getCpf())).thenReturn(clientResponse);
        when(vehicleService.findById(vehicleResponse.getId())).thenReturn(vehicleResponse);
        when(observationService.findAllByIdIn(Collections.singletonList(observationResponse.getId())))
                .thenReturn(Collections.singletonList(observationResponse));
        when(specificServiceService.findAllByIdIn(Collections.singletonList(specificServiceResponse.getId())))
                .thenReturn(Collections.singletonList(specificServiceResponse));
        when(generalServiceService.findAllByIdIn(Collections.singletonList(generalServiceResponse.getId())))
                .thenReturn(Collections.singletonList(generalServiceResponse));

        mockMvc.perform(mockGetRequest(orderOfService.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.diagnosticId").value(orderOfService.getDiagnosticId()))
                .andExpect(jsonPath("$.clientResponse.id").isString())
                .andExpect(jsonPath("$.vehicleResponse.id").isString())
                .andExpect(jsonPath("$.observationResponses[0].id")
                        .value(observationResponse.getId().toString()))
                .andExpect(jsonPath("$.specificServiceResponses[0].id")
                        .value(specificServiceResponse.getId().toString()))
                .andExpect(jsonPath("$.generalServiceResponses[0].id")
                        .value(generalServiceResponse.getId().toString()));
    }

}