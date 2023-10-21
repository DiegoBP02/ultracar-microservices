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
public class ObservationDTO {
    @NotBlank
    private String name;
    @NotNull
    private Situation situation;
}