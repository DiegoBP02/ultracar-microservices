package com.example.vehicleservice;

import com.example.vehicleservice.entities.Vehicle;
import com.example.vehicleservice.enums.Accessory;
import com.example.vehicleservice.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public void run(String... args) throws Exception {
        createVehicle1();
        createVehicle2();
        createVehicle3();
        createVehicle4();
    }

    private void createVehicle1() {
        Vehicle vehicle1 = Vehicle.builder()
                .clientCpf("11111111111")
                .licensePlate("GHG-1234")
                .model("Ford Fiesta")
                .year("2019")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        Vehicle savedVehicle = vehicleRepository.save(vehicle1);
        System.out.println("Vehicle saved: " + savedVehicle.getId());
    }

    private void createVehicle2() {
        Vehicle vehicle2 = Vehicle.builder()
                .clientCpf("22222222222")
                .licensePlate("GHG-1235")
                .model("Volkswagen Gol")
                .year("2018")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        vehicleRepository.save(vehicle2);
    }

    private void createVehicle3() {
        Vehicle vehicle3 = Vehicle.builder()
                .clientCpf("333333333")
                .licensePlate("GHG-1236")
                .model("Ford Fiesta")
                .year("2019")
                .accessories(Collections.singletonList(Accessory.AIRBAG))
                .build();
        vehicleRepository.save(vehicle3);
    }

    private void createVehicle4() {
        Vehicle vehicle4 = Vehicle.builder()
                .clientCpf("11111111111")
                .licensePlate("GHG-1237")
                .model("Volkswagen Gol")
                .year("2018")
                .accessories(List.of(Accessory.AIRBAG, Accessory.GPS))
                .build();
        vehicleRepository.save(vehicle4);
    }
}
