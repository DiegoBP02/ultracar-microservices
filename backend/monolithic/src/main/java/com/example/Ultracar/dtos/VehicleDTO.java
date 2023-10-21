package com.example.Ultracar.dtos;

import com.example.Ultracar.enums.Accessory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    @NotBlank
    private String licensePlate;
    @NotBlank
    @Size(min = 4, max = 4, message = "Year must have exactly 4 digits")
    @Pattern(regexp = "\\d{4}", message = "Year must contain only numeric characters")
    private String year;
    @NotBlank
    private String model;
    private List<Accessory> accessories;
    @NotBlank
    private String clientCpf;
}