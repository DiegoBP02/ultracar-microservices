package com.example.Ultracar.services;

import com.example.Ultracar.dtos.SpecificServiceDTO;
import com.example.Ultracar.entities.SpecificService;
import com.example.Ultracar.exceptions.UniqueConstraintViolationException;
import com.example.Ultracar.repositories.SpecificServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SpecificServiceService {

    @Autowired
    private SpecificServiceRepository specificServiceRepository;

    public SpecificService create(SpecificServiceDTO specificServiceDTO) {
        try {
            SpecificService specificService = SpecificService.builder()
                    .serviceName(specificServiceDTO.getServiceName())
                    .vehicleModel(specificServiceDTO.getVehicleModel())
                    .situation(specificServiceDTO.getSituation())
                    .build();
            return specificServiceRepository.save(specificService);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    public List<SpecificService> findAllSpecificServiceByVehicleModel(String vehicleModel) {
        return specificServiceRepository.findAllByVehicleModel(vehicleModel);
    }

    public List<SpecificService> findAllByIdIn(List<UUID> ids) {
        return specificServiceRepository.findAllByIdIn(ids);
    }

}
