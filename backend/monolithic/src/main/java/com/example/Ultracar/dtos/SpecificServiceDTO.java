package com.example.Ultracar.dtos;

import com.example.Ultracar.enums.Situation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificServiceDTO {
    @NotBlank
    private String serviceName;
    @NotBlank
    private String vehicleModel;
    @NotNull
    private Situation situation;
}