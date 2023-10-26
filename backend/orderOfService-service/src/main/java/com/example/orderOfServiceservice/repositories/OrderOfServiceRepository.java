package com.example.orderOfServiceservice.repositories;

import com.example.orderOfServiceservice.entities.OrderOfService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderOfServiceRepository extends JpaRepository<OrderOfService, UUID> {
}
