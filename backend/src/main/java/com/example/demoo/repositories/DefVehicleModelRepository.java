package com.example.demoo.repositories;

import com.example.demoo.domain.DefVehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefVehicleModelRepository extends JpaRepository<DefVehicleModel, Long> {
    DefVehicleModel findByName(String name);

    Optional<DefVehicleModel> findModelByName(String name);
}