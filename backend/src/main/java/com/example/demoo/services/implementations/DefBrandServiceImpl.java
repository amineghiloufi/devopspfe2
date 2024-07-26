package com.example.demoo.services.implementations;

import com.example.demoo.domain.DefBrand;
import com.example.demoo.domain.DefVehicleCategory;
import com.example.demoo.exceptions.domain.BrandNotFoundException;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import com.example.demoo.repositories.DefBrandRepository;
import com.example.demoo.repositories.DefVehicleCategoryRepository;
import com.example.demoo.repositories.DefVehicleModelRepository;
import com.example.demoo.services.DefBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefBrandServiceImpl implements DefBrandService {

    private final DefBrandRepository defBrandRepository;
    private final DefVehicleCategoryRepository defVehicleCategoryRepository;
    private final DefVehicleModelRepository defVehicleModelRepository;

    @Autowired
    public DefBrandServiceImpl(DefBrandRepository defBrandRepository,
                               DefVehicleCategoryRepository defVehicleCategoryRepository,
                               DefVehicleModelRepository defVehicleModelRepository) {

        this.defBrandRepository = defBrandRepository;
        this.defVehicleCategoryRepository = defVehicleCategoryRepository;
        this.defVehicleModelRepository = defVehicleModelRepository;
    }

    @Override
    public List<DefBrand> findAll() {
        return defBrandRepository.findAll();
    }

    @Override
    public DefBrand findById(Long id) {
        return defBrandRepository.findById(id).orElse(null);
    }

    @Override
    public DefBrand add(String brandName, List<String> categoryNames) throws CategoryNotFoundException {
        DefBrand brand = new DefBrand();
        brand.setBrandName(brandName);
        defBrandRepository.save(brand);
        List<DefVehicleCategory> categoriesToAdd = new ArrayList<>();
        List<String> nonExistentCategories = new ArrayList<>();
        for (String categoryName : categoryNames) {
            DefVehicleCategory category = defVehicleCategoryRepository.findByCategoryName(categoryName);
            if (category != null) {
                categoriesToAdd.add(category);
            } else {
                nonExistentCategories.add(categoryName);
            }
        }
        if (!nonExistentCategories.isEmpty()) {
            String errorMessage = "The following categories do not exist: " + String.join(", ",
                    nonExistentCategories);
            throw new CategoryNotFoundException(errorMessage);
        }
        for (DefVehicleCategory category : categoriesToAdd) {
            category.getBrands().add(brand);
            brand.getCategories().add(category);
        }
        defVehicleCategoryRepository.saveAll(categoriesToAdd);
        defBrandRepository.save(brand);
        return brand;
    }

    @Override
    public DefBrand updateBrandName(String currentBrandName, String newBrandName) throws BrandNotFoundException {
        Optional<DefBrand> optionalBrand = defBrandRepository.findBrandByBrandName(currentBrandName);
        if (optionalBrand.isEmpty()) {
            throw new BrandNotFoundException("No brand found by the name : "+ currentBrandName +", please try again.");
        }
        DefBrand brand = optionalBrand.get();
        List<DefVehicleCategory> copiedCategories = new ArrayList<>(brand.getCategories());
        for (DefVehicleCategory category : copiedCategories) {
            category.getBrands().remove(brand);
        }
        brand.getCategories().clear();
        brand.setBrandName(newBrandName);
        DefBrand updatedBrand = defBrandRepository.save(brand);
        for (DefVehicleCategory category : copiedCategories) {
            category.getBrands().add(updatedBrand);
            updatedBrand.getCategories().add(category);
        }
        defVehicleCategoryRepository.saveAll(copiedCategories);
        return updatedBrand;
    }

    @Override
    public DefBrand moveBrand(String currentBrandName, String fromCategoryName, String toCategoryName)
            throws BrandNotFoundException, CategoryNotFoundException {
        Optional<DefBrand> optionalBrand = defBrandRepository.findBrandByBrandName(currentBrandName);
        if (optionalBrand.isEmpty()) {
            throw new BrandNotFoundException("No brand found by the name : " + currentBrandName + ", please try again.");
        }
        DefBrand brand = optionalBrand.get();
        Optional<DefVehicleCategory> optionalFromCategory = defVehicleCategoryRepository.findCategoryByCategoryName(fromCategoryName);
        Optional<DefVehicleCategory> optionalToCategory = defVehicleCategoryRepository.findCategoryByCategoryName(toCategoryName);
        if (optionalFromCategory.isEmpty()) {
            throw new CategoryNotFoundException("No category found by the name : " + fromCategoryName + ", please try again.");
        }
        if (optionalToCategory.isEmpty()) {
            throw new CategoryNotFoundException("No category found by the name : " + toCategoryName + ", please try again.");
        }
        DefVehicleCategory fromCategory = optionalFromCategory.get();
        DefVehicleCategory toCategory = optionalToCategory.get();
        fromCategory.getBrands().remove(brand);
        toCategory.getBrands().add(brand);
        brand.getCategories().remove(fromCategory);
        brand.getCategories().add(toCategory);
        defBrandRepository.save(brand);
        defVehicleCategoryRepository.saveAll(List.of(fromCategory, toCategory));
        return brand;
    }

    @Override
    public DefBrand addToCategory(String currentBrandName, String categoryName)
            throws BrandNotFoundException, CategoryNotFoundException {

        Optional<DefBrand> optionalBrand = defBrandRepository.findBrandByBrandName(currentBrandName);
        if (optionalBrand.isEmpty()) {
            throw new BrandNotFoundException("No brand found by the name : " + currentBrandName + ", please try again.");
        }
        DefBrand brand = optionalBrand.get();
        Optional<DefVehicleCategory> optionalCategory = defVehicleCategoryRepository.findCategoryByCategoryName(categoryName);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("No category found by the name : " + categoryName + ", please try again.");
        }
        DefVehicleCategory category = optionalCategory.get();
        if (brand.getCategories().contains(category)) {
            throw new IllegalArgumentException("Brand is already associated with " + categoryName);
        }
        brand.getCategories().add(category);
        defBrandRepository.save(brand);
        category.getBrands().add(brand);
        defVehicleCategoryRepository.save(category);
        return brand;
    }

    @Override
    public DefBrand removeFromCategory(String currentBrandName, String currentCategoryName)
            throws BrandNotFoundException, CategoryNotFoundException {

        Optional<DefBrand> optionalBrand = defBrandRepository.findBrandByBrandName(currentBrandName);
        if (optionalBrand.isEmpty()) {
            throw new BrandNotFoundException("No brand found by the name: " + currentBrandName + ", please try again.");
        }
        DefBrand brand = optionalBrand.get();
        Optional<DefVehicleCategory> optionalCategory = defVehicleCategoryRepository.findCategoryByCategoryName(currentCategoryName);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("No category found by the name: " + currentCategoryName + ", please try again.");
        }
        DefVehicleCategory category = optionalCategory.get();
        if (!brand.getCategories().contains(category)) {
            throw new IllegalArgumentException(currentBrandName +  "is not associated with category: " + currentCategoryName);
        }
        brand.getCategories().remove(category);
        category.getBrands().remove(brand);
        defBrandRepository.save(brand);
        defVehicleCategoryRepository.save(category);
        return brand;
    }

    @Override
    public void delete(Long brandId) throws BrandNotFoundException {
        Optional<DefBrand> optionalBrand = defBrandRepository.findById(brandId);
        if (optionalBrand.isEmpty()) {
            throw new BrandNotFoundException("No brand found with this ID.");
        }
        DefBrand brand = optionalBrand.get();
        List<DefVehicleCategory> categories = new ArrayList<>(brand.getCategories());
        for (DefVehicleCategory category : categories) {
            category.getBrands().remove(brand);
            category.getModels().clear();
        }
        defVehicleModelRepository.deleteAll(brand.getModels());
        defBrandRepository.delete(brand);
    }
}
