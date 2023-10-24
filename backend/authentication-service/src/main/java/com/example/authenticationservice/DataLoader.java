package com.example.authenticationservice;

import com.example.authenticationservice.entities.User;
import com.example.authenticationservice.enums.Role;
import com.example.authenticationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdmin();
    }

    private void createAdmin() {
        User admin = User.builder()
                .name("admin")
                .password(passwordEncoder.encode("senha"))
                .role(Role.ADMIN)
                .build();
        User savedAdmin = userRepository.save(admin);
        System.out.println("Admin saved: " + savedAdmin.getId());
    }
}