package com.example.demoo.services;

import com.example.demoo.domain.DefVehicleModel;
import com.example.demoo.exceptions.domain.BrandNotFoundException;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import com.example.demoo.exceptions.domain.ModelNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DefVehicleModelService {

    List<DefVehicleModel> findAll();

    DefVehicleModel findById(Long id);

    DefVehicleModel createModel(String brandName, String categoryName, String name, String version, String year, String horsePower)
            throws BrandNotFoundException, CategoryNotFoundException;

    DefVehicleModel deactivate(Long modelId) throws ModelNotFoundException;

    void deleteById(Long modelId) throws ModelNotFoundException;
}