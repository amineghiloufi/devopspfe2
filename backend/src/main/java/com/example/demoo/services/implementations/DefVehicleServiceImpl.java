package com.example.demoo.services.implementations;

import com.example.demoo.domain.DefUser;
import com.example.demoo.domain.DefVehicle;
import com.example.demoo.domain.DefVehicleModel;
import com.example.demoo.enumeratation.VehicleStatus;
import com.example.demoo.exceptions.domain.ModelNotFoundException;
import com.example.demoo.exceptions.domain.UserNotFoundException;
import com.example.demoo.exceptions.domain.VehicleNotFoundException;
import com.example.demoo.repositories.DefUserRepository;
import com.example.demoo.repositories.DefVehicleModelRepository;
import com.example.demoo.repositories.DefVehicleRepository;
import com.example.demoo.services.DefVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DefVehicleServiceImpl implements DefVehicleService {

    private final DefVehicleRepository defVehicleRepository;

    private final DefUserRepository defUserRepository;

    private final DefVehicleModelRepository defVehicleModelRepository;

    @Autowired
    public DefVehicleServiceImpl(DefVehicleRepository defVehicleRepository,
                                 DefVehicleModelRepository defVehicleModelRepository,
                                 DefUserRepository defUserRepository) {
        this.defVehicleRepository = defVehicleRepository;
        this.defVehicleModelRepository = defVehicleModelRepository;
        this.defUserRepository = defUserRepository;
    }

    @Override
    public List<DefVehicle> findAll() {
        return defVehicleRepository.findAll();
    }

    @Override
    public DefVehicle findById(Long id) {
        return defVehicleRepository.findById(id).orElse(null);
    }

    @Override
    public DefVehicle addNewVehicle(String agencyName, String modelName, String registrationNumber,
                                    Float price, String color)
            throws UserNotFoundException, ModelNotFoundException {
        Optional<DefUser> optionalAgency = defUserRepository.findAgencyByAgencyName(agencyName);
        if (optionalAgency.isEmpty()) {
            throw  new UserNotFoundException("No agency found by this name. Please try again.");
        }
        Optional<DefVehicleModel> optionalModel = defVehicleModelRepository.findModelByName(modelName);
        if (optionalModel.isEmpty()) {
            throw new ModelNotFoundException("No model found by this name, Please try again.");
        }
        DefUser agency = optionalAgency.get();
        DefVehicleModel model = optionalModel.get();
        DefVehicle vehicle = new DefVehicle();
        vehicle.setUser(agency);
        agency.getVehicles().add(vehicle);
        vehicle.setModel(model);
        model.getVehicles().add(vehicle);
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setColor(color);
        String description = color + " " + model.getBrand().getBrandName() + " " + model.getName() + " version "
                + model.getVersion() + " of " + model.getYear() + " with a power of " + model.getHorsePower() + " HP.";
        vehicle.setDescription(description);
        vehicle.setPrice(price);
        String vehicleStatus = "Available";
        vehicle.setVehicleStatus(getVehicleStatusEnumName(vehicleStatus).name());
        defVehicleRepository.save(vehicle);
        defUserRepository.save(agency);
        defVehicleModelRepository.save(model);
        return vehicle;
    }
    @Override
    public  DefVehicle updateStatus(Long vehicleId, String newStatus)
            throws VehicleNotFoundException {
        Optional<DefVehicle> optionalVehicle = defVehicleRepository.findByVehicleId(vehicleId);
        if (optionalVehicle.isEmpty()) {
            throw new VehicleNotFoundException("No vehicle found by this ID, please try again.");
        }
        DefVehicle vehicle = optionalVehicle.get();
        vehicle.setVehicleStatus(getVehicleStatusEnumName(newStatus).name());
        defVehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public void deleteById(Long vehicleId) throws VehicleNotFoundException {
        Optional<DefVehicle> optionalVehicle = defVehicleRepository.findByVehicleId(vehicleId);
        if (optionalVehicle.isEmpty()) {
            throw new VehicleNotFoundException("No vehicle found by this ID. Please try again.");
        }
        DefVehicle vehicle = optionalVehicle.get();
        DefVehicleModel model = vehicle.getModel();
        model.getVehicles().remove(vehicle);
        defVehicleModelRepository.save(model);
        DefUser agency = vehicle.getUser();
        agency.getVehicles().remove(vehicle);
        defUserRepository.save(agency);
        defVehicleRepository.deleteById(vehicleId);
    }

    private VehicleStatus getVehicleStatusEnumName(String vehicleStatus) {
        return VehicleStatus.valueOf(vehicleStatus);
    }
}