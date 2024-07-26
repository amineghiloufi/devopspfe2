package com.example.demoo.controllers;

import com.example.demoo.domain.DefVehicleModel;
import com.example.demoo.domain.HttpResponse;
import com.example.demoo.exceptions.domain.BrandNotFoundException;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import com.example.demoo.exceptions.domain.ModelNotFoundException;
import com.example.demoo.services.DefVehicleModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/models")
public class DefVehicleModelController {

    private final DefVehicleModelService defVehicleModelService;

    @Autowired
    public DefVehicleModelController(DefVehicleModelService defVehicleModelService) {
        this.defVehicleModelService = defVehicleModelService;
    }

    @GetMapping
    public List<DefVehicleModel> getAllDefVehicleModels() {
        return defVehicleModelService.findAll();
    }

    @GetMapping("/{id}")
    public DefVehicleModel getDefVehicleModelById(@PathVariable Long id) {
        return defVehicleModelService.findById(id);
    }

    @PostMapping("/add")
    public DefVehicleModel createDefVehicleModel(@RequestParam("brand") String brandName,
                                                 @RequestParam("category") String categoryName,
                                                 @RequestParam("modelName") String name,
                                                 @RequestParam("version") String version,
                                                 @RequestParam("year") String year,
                                                 @RequestParam("horsePower") String horsePower)
            throws BrandNotFoundException, CategoryNotFoundException {
        return defVehicleModelService.createModel(brandName, categoryName, name, version, year, horsePower);
    }

    @PutMapping("/deactivation/{modelId}")
    public DefVehicleModel deactivate(@PathVariable Long modelId)
            throws ModelNotFoundException {
        return defVehicleModelService.deactivate(modelId);
    }

    @DeleteMapping("/delete/{modelId}")
    public ResponseEntity<HttpResponse> deleteDefVehicleModel(@PathVariable Long modelId) throws ModelNotFoundException {
        defVehicleModelService.deleteById(modelId);
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