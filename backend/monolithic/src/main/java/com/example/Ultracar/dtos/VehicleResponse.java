package com.example.Ultracar.dtos;

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
public class VehicleResponse {
    private UUID id;
    private String licensePlate;
    private String year;
    private String model;
    private List<Accessory> accessories;
    private String clientCpf;
    public enum Accessory {
        AIRBAG,
        GPS,
        BLUETOOTH,
        SISTEMA_DE_SOM,
        CAMERA_RE,
        SENSOR_ESTACIONAMENTO,
        RODAS_LIGA_LEVE,
        TETO_SOLAR,
        BANCOS_AQUECIDOS;
    }


}