package com.example.Ultracar.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOfServiceDTO {
    @NotNull
    private String clientCpf;
    @NotNull
    private UUID vehicleId;
    private List<UUID> specificServiceIds;
    private List<UUID> generalServiceIds;
    private List<UUID> observationIds;
}