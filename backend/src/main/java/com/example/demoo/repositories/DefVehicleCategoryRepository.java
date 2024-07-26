package com.example.demoo.repositories;

import com.example.demoo.domain.DefVehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DefVehicleCategoryRepository extends JpaRepository<DefVehicleCategory, Long> {
    DefVehicleCategory findByCategoryName(String categoryName);

    Optional<DefVehicleCategory> findCategoryByCategoryName(String categoryName);

    List<DefVehicleCategory> findByCategoryType(String categoryType);
}