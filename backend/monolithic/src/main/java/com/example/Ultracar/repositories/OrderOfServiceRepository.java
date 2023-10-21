package com.example.Ultracar.repositories;

import com.example.Ultracar.entities.OrderOfService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderOfServiceRepository extends JpaRepository<OrderOfService, UUID> {
}
