package com.example.generalServiceservice.services;

import com.example.generalServiceservice.dtos.GeneralServiceDTO;
import com.example.generalServiceservice.entities.GeneralService;
import com.example.generalServiceservice.exceptions.ResourceNotFoundException;
import com.example.generalServiceservice.exceptions.UniqueConstraintViolationException;
import com.example.generalServiceservice.repositories.GeneralServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GeneralServiceService {

    @Autowired
    private GeneralServiceRepository generalServiceRepository;

    public GeneralService create(GeneralServiceDTO generalServiceDTO) {
        try {
            GeneralService generalService = GeneralService.builder()
                    .serviceName(generalServiceDTO.getServiceName())
                    .situation(generalServiceDTO.getSituation())
                    .build();
            return generalServiceRepository.save(generalService);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    public List<GeneralService> findAll() {
        return generalServiceRepository.findAll();
    }

    public GeneralService findById(UUID id) {
        return generalServiceRepository.findById(id)
                .orElseThrow(() -> new
                        ResourceNotFoundException("General service not found. ID: " + id));
    }

    public List<GeneralService> findAllByIdIn(List<UUID> ids) {
        return generalServiceRepository.findAllByIdIn(ids);
    }
}
