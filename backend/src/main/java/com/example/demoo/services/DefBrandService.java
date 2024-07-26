package com.example.demoo.services;

import com.example.demoo.domain.DefBrand;
import com.example.demoo.exceptions.domain.BrandNotFoundException;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DefBrandService {

    List<DefBrand> findAll();

    DefBrand findById(Long id);

    DefBrand add(String brandName, List<String> categoryNames) throws CategoryNotFoundException;

    DefBrand updateBrandName(String currentBrandName, String newBrandName) throws BrandNotFoundException;

    DefBrand moveBrand(String currentBrandName, String fromCategoryName, String toCategoryName)
            throws BrandNotFoundException, CategoryNotFoundException;

    DefBrand addToCategory(String currentBrandName, String categoryName)
            throws BrandNotFoundException, CategoryNotFoundException;

    DefBrand removeFromCategory(String currentBrandName, String categoryName)
            throws BrandNotFoundException, CategoryNotFoundException;

    void delete(Long brandId) throws BrandNotFoundException;
}