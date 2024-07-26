package com.example.demoo.services;

import com.example.demoo.domain.DefVehicle;
import com.example.demoo.exceptions.domain.ModelNotFoundException;
import com.example.demoo.exceptions.domain.UserNotFoundException;
import com.example.demoo.exceptions.domain.VehicleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DefVehicleService {

         List<DefVehicle> findAll();

         DefVehicle findById(Long id);

         DefVehicle addNewVehicle(String agencyName, String modelName, String registrationNumber,
                             Float price, String color)
                 throws UserNotFoundException, ModelNotFoundException;

         DefVehicle updateStatus(Long vehicleId, String newStatus)
                 throws VehicleNotFoundException;

         void deleteById(Long vehicleId) throws VehicleNotFoundException;
}
