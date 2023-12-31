package com.example.vehicleservice.entities;

import com.example.vehicleservice.enums.Accessory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "t_vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String licensePlate;
    @Column(nullable = false)
    private String year;
    @Column(nullable = false)
    private String model;
    @ElementCollection(targetClass = Accessory.class)
    @CollectionTable(name = "car_accessories", joinColumns = @JoinColumn(name = "car_id"))
    @Enumerated(EnumType.STRING)
    private List<Accessory> accessories;
    @Column(nullable = false, name = "client_cpf")
    private String clientCpf;
}
