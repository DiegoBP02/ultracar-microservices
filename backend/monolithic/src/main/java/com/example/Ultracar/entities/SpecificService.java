package com.example.Ultracar.entities;

import com.example.Ultracar.enums.Situation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "t_specific_service", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serviceName", "situation", "vehicleModel"})
})
public class SpecificService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String serviceName;
    @Column(nullable = false)
    private String vehicleModel;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situation situation;
}
