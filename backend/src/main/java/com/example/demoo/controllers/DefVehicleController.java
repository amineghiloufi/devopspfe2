package com.example.demoo.controllers;

import com.example.demoo.domain.DefVehicle;
import com.example.demoo.domain.HttpResponse;
import com.example.demoo.exceptions.domain.ModelNotFoundException;
import com.example.demoo.exceptions.domain.UserNotFoundException;
import com.example.demoo.exceptions.domain.VehicleNotFoundException;
import com.example.demoo.services.implementations.DefVehicleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class DefVehicleController {

    private final DefVehicleServiceImpl vehicleService;

    @Autowired
    public DefVehicleController(DefVehicleServiceImpl vehicleService) {

        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<DefVehicle> findAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public DefVehicle findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @PostMapping("/add")
    public DefVehicle addNewVehicle(@RequestParam("agency") String agencyName,
                                    @RequestParam("model") String modelName,
                                    @RequestParam("registrationNumber") String registrationNumber,
                                    @RequestParam("price") Float price,
                                    @RequestParam("color") String color)
            throws ModelNotFoundException, UserNotFoundException {
        return vehicleService.addNewVehicle(agencyName, modelName, registrationNumber, price, color);
    }

    @PutMapping("/updateStatus/{vehicleId}")
    public DefVehicle updateVehicle(@PathVariable Long vehicleId,
                                    @RequestParam("newStatus") String newStatus)
            throws VehicleNotFoundException {
        return vehicleService.updateStatus(vehicleId, newStatus);
    }

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable Long vehicleId)
            throws VehicleNotFoundException {
        vehicleService.deleteById(vehicleId);
        return response(HttpStatus.OK, "Item is successfully deleted");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(),
                message);
        return new ResponseEntity<>(body, httpStatus);
    }
}