package com.example.generalServiceservice.dtos;

import com.example.generalServiceservice.enums.Situation;
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
public class GeneralServiceDTO {
    @NotBlank
    private String serviceName;
    @NotNull
    private Situation situation;
}