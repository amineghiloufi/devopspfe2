package com.example.demoo.repositories;

import com.example.demoo.domain.DefVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefVehicleRepository extends JpaRepository<DefVehicle, Long> {

    Optional<DefVehicle> findByVehicleId(Long vehicleId);

    Optional<DefVehicle> findVehicleByRegistrationNumber(String registrationNumber);
}