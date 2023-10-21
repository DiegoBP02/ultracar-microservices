package com.example.Ultracar.dtos;

import com.example.Ultracar.enums.Accessory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {
    private String licensePlate;
    private String year;
    private String model;
    private List<Accessory> accessories;
}