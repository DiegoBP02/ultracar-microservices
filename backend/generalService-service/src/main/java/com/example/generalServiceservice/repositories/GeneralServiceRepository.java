package com.example.generalServiceservice.repositories;

import com.example.generalServiceservice.entities.GeneralService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GeneralServiceRepository extends JpaRepository<GeneralService, UUID> {
    List<GeneralService> findAllByIdIn(List<UUID> ids);
}
