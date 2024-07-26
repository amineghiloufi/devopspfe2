package com.example.demoo.services;

import com.example.demoo.domain.DefVehicleCategory;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DefVehicleCategoryService {

    DefVehicleCategory findCategoryByName(String categoryName);

    List<DefVehicleCategory> findAll();

    DefVehicleCategory findById(Long id);

    List<DefVehicleCategory> findByCategoryType(String categoryType);

    DefVehicleCategory createCategory(String categoryName, String categoryType);

    DefVehicleCategory update(String currentCategoryName, String categoryName , String categoryType);

    void deleteById(Long categoryId) throws CategoryNotFoundException;
}