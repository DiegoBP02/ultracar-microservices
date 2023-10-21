package com.example.Ultracar.repositories;

import com.example.Ultracar.entities.SpecificService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpecificServiceRepository extends JpaRepository<SpecificService, UUID> {
    List<SpecificService> findAllByVehicleModel(String vehicleModel);

    List<SpecificService> findAllByIdIn(List<UUID> ids);
}
