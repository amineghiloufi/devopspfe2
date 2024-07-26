package com.example.demoo.services;

import com.example.demoo.domain.Contract;
import com.example.demoo.exceptions.domain.UserNotFoundException;
import com.example.demoo.exceptions.domain.VehicleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ContractService {

    List<Contract> findAll();

    Contract findById(Long id);

    Contract createContract(Long vehicleId, String fullName, Date pickDateReservation, Date returnDateReservation)
            throws VehicleNotFoundException, UserNotFoundException;

    void deleteById(Long id);
}
