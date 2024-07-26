package com.example.demoo.controllers;

import com.example.demoo.domain.DefVehicleCategory;
import com.example.demoo.domain.HttpResponse;
import com.example.demoo.enumeratation.CategoryType;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import com.example.demoo.repositories.DefVehicleCategoryRepository;
import com.example.demoo.services.DefVehicleCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
@RequestMapping("/categories")
public class DefVehicleCategoryController {

    private final DefVehicleCategoryService defVehicleCategoryService;
    private final DefVehicleCategoryRepository defVehicleCategoryRepository;

    @Autowired
    public DefVehicleCategoryController(DefVehicleCategoryService defVehicleCategoryService,
                                        DefVehicleCategoryRepository defVehicleCategoryRepository) {
        this.defVehicleCategoryService = defVehicleCategoryService;
        this.defVehicleCategoryRepository = defVehicleCategoryRepository;
    }

    @GetMapping
    public List<DefVehicleCategory> getAllDefVehicleCategories() {

        return defVehicleCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public DefVehicleCategory getDefVehicleCategoryById(@PathVariable Long id) {
        return defVehicleCategoryService.findById(id);
    }

    @GetMapping("/ofTourism")
    public List<DefVehicleCategory> getCategoriesOfTourismType() {
        return defVehicleCategoryService.findByCategoryType((CategoryType.Tourism).toString());
    }

    @GetMapping("/ofUtility")
    public List<DefVehicleCategory> getCategoriesOfUtilityType() {
        return defVehicleCategoryService.findByCategoryType((CategoryType.Utility).toString());
    }

    @PostMapping("/add")
    public DefVehicleCategory createDefVehicleCategory(@RequestParam("categoryName") String categoryName,
                                                       @RequestParam("categoryType") String categoryType) {
        return defVehicleCategoryService.createCategory(categoryName, categoryType);
    }

    @PutMapping("/update")
    public DefVehicleCategory updateDefVehicleCategory(@RequestParam("currentCategoryName") String currentCategoryName,
                                                       @RequestParam("categoryName") String categoryName,
                                                       @RequestParam("categoryType") String categoryType) {
        return defVehicleCategoryService.update(currentCategoryName, categoryName, categoryType);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable("categoryId") Long categoryId)
            throws CategoryNotFoundException {
        defVehicleCategoryService.deleteById(categoryId);
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