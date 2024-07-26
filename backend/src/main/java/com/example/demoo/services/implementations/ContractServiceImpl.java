package com.example.demoo.services.implementations;

import com.example.demoo.domain.Contract;
import com.example.demoo.domain.DefUser;
import com.example.demoo.domain.DefVehicle;
import com.example.demoo.enumeratation.VehicleStatus;
import com.example.demoo.exceptions.domain.UserNotFoundException;
import com.example.demoo.exceptions.domain.VehicleNotFoundException;
import com.example.demoo.repositories.*;
import com.example.demoo.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;


    private final DefUserRepository defUserRepository;

    private final DefVehicleRepository defVehicleRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository,
                               DefUserRepository defUserRepository,
                               DefVehicleRepository defVehicleRepository) {

        this.contractRepository = contractRepository;
        this.defUserRepository = defUserRepository;
        this.defVehicleRepository = defVehicleRepository;
    }

    @Override
    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    @Override
    public Contract findById(Long id) {
        return contractRepository.findById(id).orElse(null);
    }

    @Override
    public Contract createContract(Long vehicleId, String fullName, Date pickDateReservation,
                                   Date returnDateReservation)
            throws VehicleNotFoundException, UserNotFoundException {
        Optional<DefVehicle> optionalVehicle = defVehicleRepository.findByVehicleId(vehicleId);
        if (optionalVehicle.isEmpty()) {
            throw new VehicleNotFoundException("No vehicle found by this ID");
        }
        DefVehicle vehicle = optionalVehicle.get();
        Optional<DefUser> optionalCustomer = defUserRepository.findUserByCustomerFullName(fullName);
        if (optionalCustomer.isEmpty()) {
            throw new UserNotFoundException("No customer found by this full name");
        }
        DefUser customer = optionalCustomer.get();
        String customerFullName = customer.getCustomerFullName();
        Contract contract = new Contract();
        contract.setVehicleID(vehicle.getVehicleId());
        contract.setVehicleDescription(vehicle.getDescription());
        DefUser agency = vehicle.getUser();
        contract.setAgencyName(vehicle.getUser().getAgencyName());
        contract.setCustomerFullName(customerFullName);
        contract.setPickDateReservation(pickDateReservation);
        contract.setReturnDateReservation(returnDateReservation);
        contract.setCreationDate(new Date());
        contract.setPrice(vehicle.getPrice());
        contract.setSignedByAgency(true);
        contract.setSignedByCustomer(true);
        contract.setActive(true);
        contractRepository.save(contract);
        customer.getContracts().add(contract);
        agency.getContracts().add(contract);
        defUserRepository.save(agency);
        defUserRepository.save(customer);
        return contract;
    }

    @Override
    public void deleteById(Long id) {
        Contract contract = contractRepository.findById(id).orElse(null);
        if (contract != null) {
            if (Objects.equals(contract.getVehicle().getVehicleStatus(), (VehicleStatus.Available).toString())) {
                contractRepository.deleteById(id);
            }
        }
    }
}