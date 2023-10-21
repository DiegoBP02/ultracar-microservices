package com.example.Ultracar.integrationTests;

import com.example.Ultracar.DataLoader;
import com.example.Ultracar.dtos.LoginDTO;
import com.example.Ultracar.dtos.RegisterDTO;
import com.example.Ultracar.entities.User;
import com.example.Ultracar.enums.Role;
import com.example.Ultracar.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
class AuthenticationIntegrationTest {

    private static final String PATH = "/auth";

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
    private PasswordEncoder passwordEncoder;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    private User admin = User.builder()
            .name("user1")
            .password("password")
            .role(Role.ADMIN)
            .build();
    private RegisterDTO registerDTO = RegisterDTO.builder()
            .name("user2")
            .password("password")
            .role(Role.EMPLOYEE)
            .build();
    private LoginDTO loginDTO = LoginDTO.builder()
            .name("user1")
            .password("password")
            .build();

    private User setupAdmin() {
        return userRepository.findByName(admin.getName())
                .orElseGet(() -> userRepository.save(admin));
    }

    private MockHttpServletRequestBuilder mockPostRequest
            (String endpoint, Object requestObject) throws Exception {
        return MockMvcRequestBuilders
                .post(PATH + "/" + endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(requestObject));
    }

    @Test
    void shouldLoginUserAndReturnToken() throws Exception {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        setupAdmin();

        mockMvc.perform(mockPostRequest("login", loginDTO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString());

        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void shouldRegisterUser() throws Exception {
        mockMvc.perform(mockPostRequest("registerUser", registerDTO).with(user(setupAdmin())))
                .andExpect(status().isCreated());

        assertEquals(2, userRepository.findAll().size());
    }
}