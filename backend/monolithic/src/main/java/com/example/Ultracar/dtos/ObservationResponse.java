package com.example.Ultracar.dtos;

import com.example.Ultracar.enums.Situation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObservationResponse {
    private UUID id;
    private String name;
    private Situation situation;
}