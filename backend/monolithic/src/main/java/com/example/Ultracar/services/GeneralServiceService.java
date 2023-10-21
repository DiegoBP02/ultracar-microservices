package com.example.Ultracar.services;

import com.example.Ultracar.dtos.GeneralServiceDTO;
import com.example.Ultracar.entities.GeneralService;
import com.example.Ultracar.exceptions.ResourceNotFoundException;
import com.example.Ultracar.exceptions.UniqueConstraintViolationException;
import com.example.Ultracar.repositories.GeneralServiceRepository;
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
