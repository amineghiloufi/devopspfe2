package com.example.demoo.services.implementations;

import com.example.demoo.domain.DefBrand;
import com.example.demoo.domain.DefVehicleCategory;
import com.example.demoo.domain.DefVehicleModel;
import com.example.demoo.exceptions.domain.BrandNotFoundException;
import com.example.demoo.exceptions.domain.CategoryNotFoundException;
import com.example.demoo.exceptions.domain.ModelNotFoundException;
import com.example.demoo.repositories.DefBrandRepository;
import com.example.demoo.repositories.DefVehicleCategoryRepository;
import com.example.demoo.repositories.DefVehicleModelRepository;
import com.example.demoo.services.DefVehicleModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefVehicleModelServiceImpl implements DefVehicleModelService {

    private final DefBrandRepository defBrandRepository;
    private final DefVehicleModelRepository defVehicleModelRepository;

    private final DefVehicleCategoryRepository defVehicleCategoryRepository;

    @Autowired
    public DefVehicleModelServiceImpl(DefBrandRepository defBrandRepository,
                                      DefVehicleModelRepository defVehicleModelRepository,
                                      DefVehicleCategoryRepository defVehicleCategoryRepository) {

        this.defBrandRepository = defBrandRepository;
        this.defVehicleCategoryRepository = defVehicleCategoryRepository;
        this.defVehicleModelRepository = defVehicleModelRepository;
    }


    @Override
    public List<DefVehicleModel> findAll() {
        return defVehicleModelRepository.findAll();
    }

    @Override
    public DefVehicleModel findById(Long id) {
        return defVehicleModelRepository.findById(id).orElse(null);
    }

    @Override
    public DefVehicleModel createModel(String brandName, String categoryName, String name, String version,
                                       String year, String horsePower)
            throws BrandNotFoundException, CategoryNotFoundException {
        Optional<DefVehicleCategory> optionalCategory = defVehicleCategoryRepository.findCategoryByCategoryName(categoryName);
        if (optionalCategory.isEmpty()){
            throw new CategoryNotFoundException("No category found by the name : " + categoryName + ", please try again.");
        }
        DefVehicleCategory category = optionalCategory.get();
        Optional<DefBrand> optionalBrand = defBrandRepository.findBrandByBrandName(brandName);
        if (optionalBrand.isEmpty()){
            throw new BrandNotFoundException("No brand found by the name : " + brandName + ", please try again.");
        }
        DefBrand brand = optionalBrand.get();
        DefVehicleModel model = new DefVehicleModel();
        model.setName(name);
        model.setBrand(brand);
        model.setCategory(category);
        model.setVersion(version);
        model.setYear(year);
        model.setHorsePower(horsePower);
        model.setActive(true);
        category.getModels().add(model);
        brand.getModels().add(model);
        defVehicleModelRepository.save(model);
        return model;
    }

    @Override
    public DefVehicleModel deactivate(Long modelId) throws ModelNotFoundException {
        Optional<DefVehicleModel> optionalModel = defVehicleModelRepository.findById(modelId);
        if (optionalModel.isEmpty()) {
            throw new ModelNotFoundException("No model found by this ID.");
        }
        DefVehicleModel model = optionalModel.get();
        model.setActive(!model.getActive());
        defVehicleModelRepository.save(model);
        return model;
    }

    @Override
    public void deleteById(Long modelId) throws ModelNotFoundException {
        Optional<DefVehicleModel> optionalModel = defVehicleModelRepository.findById(modelId);
        if (optionalModel.isEmpty()){
            throw new ModelNotFoundException("No model found by this ID");
        }
       DefVehicleModel model = optionalModel.get();
        DefBrand brand = model.getBrand();
        DefVehicleCategory category = model.getCategory();
        brand.getModels().remove(model);
        defBrandRepository.save(brand);
        category.getModels().remove(model);
        defVehicleCategoryRepository.save(category);
        defVehicleModelRepository.deleteById(modelId);
    }
}
