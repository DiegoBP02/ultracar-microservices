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
@Table(name = "t_general_service", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serviceName", "situation"})
})
public class GeneralService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String serviceName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situation situation;
}
