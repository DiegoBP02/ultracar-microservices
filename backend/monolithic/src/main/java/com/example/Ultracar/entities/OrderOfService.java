package com.example.Ultracar.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_order_of_service")
public class OrderOfService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private String diagnosticId;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    @ManyToMany
    @JoinTable(
            name = "order_specific_services",
            inverseJoinColumns = @JoinColumn(name = "specific_service_id")
    )
    private List<SpecificService> specificServices;
    @ManyToMany
    @JoinTable(
            name = "order_general_services",
            inverseJoinColumns = @JoinColumn(name = "general_service_id")
    )
    private List<GeneralService> generalServices;
    @ManyToMany
    @JoinTable(
            name = "order_observations",
            inverseJoinColumns = @JoinColumn(name = "observation_id")
    )
    private List<Observation> observations;
}
