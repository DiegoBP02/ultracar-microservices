package com.example.Ultracar.entities;

import com.example.Ultracar.enums.Accessory;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
