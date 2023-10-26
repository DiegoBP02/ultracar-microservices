package com.example.orderOfServiceservice.controller;

import com.example.orderOfServiceservice.dtos.OrderOfServiceDTO;
import com.example.orderOfServiceservice.dtos.OrderOfServiceResponse;
import com.example.orderOfServiceservice.services.OrderOfServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/orderOfService")
public class OrderOfServiceController {

    @Autowired
    private OrderOfServiceService orderOfServiceService;

    @PostMapping
    public ResponseEntity<OrderOfServiceResponse> create
            (@Valid @RequestBody OrderOfServiceDTO orderOfServiceDTO) {
        OrderOfServiceResponse orderOfServiceResponse
                = orderOfServiceService.create(orderOfServiceDTO);

        return ResponseEntity.status(201).body(orderOfServiceResponse);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderOfServiceResponse> findById(@PathVariable("id") UUID id) {
        OrderOfServiceResponse orderOfService = orderOfServiceService.findById(id);
        return ResponseEntity.ok().body(orderOfService);
    }

}