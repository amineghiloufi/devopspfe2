package com.example.demoo.services.implementations;

import com.example.demoo.domain.DefBrand;
import com.example.demoo.domain.DefVehicleCategory;
import com.example.demoo.domain.DefVehicleModel;
import com.example.demoo.enumeratation.CategoryType;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import com.example.demoo.repositories.DefBrandRepository;
import com.example.demoo.repositories.DefVehicleCategoryRepository;
import com.example.demoo.repositories.DefVehicleModelRepository;
import com.example.demoo.services.DefVehicleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefVehicleCategoryServiceImpl implements DefVehicleCategoryService {

    private final DefVehicleCategoryRepository defVehicleCategoryRepository;

    private final DefBrandRepository defBrandRepository;

    private final DefVehicleModelRepository defVehicleModelRepository;

    @Autowired
    public DefVehicleCategoryServiceImpl(DefVehicleCategoryRepository defVehicleCategoryRepository,
                                         DefBrandRepository defBrandRepository,
                                         DefVehicleModelRepository defVehicleModelRepository) {
        this.defVehicleCategoryRepository = defVehicleCategoryRepository;
        this.defBrandRepository = defBrandRepository;
        this.defVehicleModelRepository = defVehicleModelRepository;
    }

    @Override
    public List<DefVehicleCategory> findAll() {
        return defVehicleCategoryRepository.findAll();
    }

    @Override
    public DefVehicleCategory findById(Long id) {

        return defVehicleCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public DefVehicleCategory findCategoryByName(String categoryName) {
        return defVehicleCategoryRepository.findByCategoryName(categoryName);
    }

    public List<DefVehicleCategory> findByCategoryType(String categoryType) {
        return defVehicleCategoryRepository.findByCategoryType(categoryType);
    }

    @Override
    public DefVehicleCategory createCategory(String categoryName, String categoryType) {
        DefVehicleCategory category = new DefVehicleCategory();
        category.setCategoryName(categoryName);
        category.setCategoryType(getCategoryTypeEnumName(categoryType).name());
        defVehicleCategoryRepository.save(category);
        return category;
    }

    @Override
    public DefVehicleCategory update(String currentCategoryName, String categoryName , String categoryType) {
        if (currentCategoryName != null) {
            DefVehicleCategory currentCategory = findCategoryByName(currentCategoryName);
            if (currentCategory != null) {
                currentCategory.setCategoryName(categoryName);
                currentCategory.setCategoryType(categoryType);
                defVehicleCategoryRepository.save(currentCategory);
                return currentCategory;
            }
            return null;
        }
        return null;
    }
    private CategoryType getCategoryTypeEnumName(String categoryType) {
        return CategoryType.valueOf(categoryType);
    }

    @Override
    public void deleteById(Long categoryId) throws CategoryNotFoundException {
        Optional<DefVehicleCategory> optionalCategory = defVehicleCategoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("No category found by this name");
        }
        DefVehicleCategory category = optionalCategory.get();
        for (DefBrand brand : category.getBrands()) {
            brand.getCategories().remove(category);
            defBrandRepository.save(brand);
        }
        for (DefVehicleModel model : category.getModels()) {
            model.setCategory(null);
            defVehicleModelRepository.save(model);
        }
        defVehicleCategoryRepository.save(category);
        defVehicleCategoryRepository.deleteById(categoryId);
    }
}