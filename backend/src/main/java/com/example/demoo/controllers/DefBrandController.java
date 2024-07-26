package com.example.demoo.controllers;

import com.example.demoo.domain.DefBrand;
import com.example.demoo.domain.HttpResponse;
import com.example.demoo.exceptions.domain.BrandNotFoundException;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import com.example.demoo.repositories.DefVehicleCategoryRepository;
import com.example.demoo.services.DefBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/brands")
public class DefBrandController {

    private final DefBrandService defBrandService;
    private final DefVehicleCategoryRepository defVehicleCategoryRepository;

    @Autowired
    public DefBrandController(DefBrandService defBrandService,
                              DefVehicleCategoryRepository defVehicleCategoryRepository)
    {
        this.defBrandService = defBrandService;
        this.defVehicleCategoryRepository = defVehicleCategoryRepository;
    }

    @GetMapping
    public List<DefBrand> getAllDefBrands() {
        return defBrandService.findAll();
    }

    @GetMapping("/{id}")
    public DefBrand getDefBrandById(@PathVariable Long id) {

        return defBrandService.findById(id);
    }

    @PostMapping("/add")
    public DefBrand createDefBrand(@RequestParam("brandName") String brandName,
                                   @RequestParam("categoryNames") List<String> categoryNames)
            throws CategoryNotFoundException {

        return defBrandService.add(brandName, categoryNames);
    }

    @PutMapping("/updateName")
    public DefBrand updateBrandName(@RequestParam("currentBrandName") String currentBrandName,
                                    @RequestParam("newBrandName") String newBrandName)
            throws BrandNotFoundException {

        return defBrandService.updateBrandName(currentBrandName, newBrandName);
    }

    @PutMapping("/move")
    public DefBrand moveBrand(@RequestParam("currentBrandName") String currentBrandName,
                              @RequestParam("fromCategoryName") String fromCategoryName,
                              @RequestParam("toCategoryName") String toCategoryName)
            throws CategoryNotFoundException, BrandNotFoundException {

        return defBrandService.moveBrand(currentBrandName, fromCategoryName, toCategoryName);
    }

    @PutMapping("/addToCategory")
    public DefBrand addToCategory(@RequestParam("currentBrandName") String currentBrandName,
                                  @RequestParam("toCategoryName") String categoryName)
            throws CategoryNotFoundException, BrandNotFoundException {

        return defBrandService.addToCategory(currentBrandName, categoryName);
    }

    @PutMapping("/removeFromCategory")
    public DefBrand removeFromCategory(@RequestParam("currentBrandName") String currentBrandName,
                                       @RequestParam("currentCategoryName") String currentCategoryName)
            throws CategoryNotFoundException, BrandNotFoundException {

        return defBrandService.removeFromCategory(currentBrandName, currentCategoryName);
    }

    @DeleteMapping("/delete/{brandId}")
    public ResponseEntity<HttpResponse> deleteBrand(@PathVariable Long brandId) throws BrandNotFoundException {
        defBrandService.delete(brandId);
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