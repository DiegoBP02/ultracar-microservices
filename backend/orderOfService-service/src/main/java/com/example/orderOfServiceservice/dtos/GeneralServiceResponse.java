package com.example.orderOfServiceservice.dtos;

import com.example.orderOfServiceservice.enums.Situation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralServiceResponse {
    private UUID id;
    private String serviceName;
    private Situation situation;
    private String vehicleModel;
}