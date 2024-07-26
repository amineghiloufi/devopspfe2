package com.example.demoo.controllers;

import com.example.demoo.domain.Contract;
import com.example.demoo.exceptions.domain.UserNotFoundException;
import com.example.demoo.exceptions.domain.VehicleNotFoundException;
import com.example.demoo.services.implementations.ContractServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractServiceImpl contractServiceImpl;

    public ContractController(ContractServiceImpl contractServiceImpl) {
        this.contractServiceImpl = contractServiceImpl;
    }

    @GetMapping
    public List<Contract> getAllContracts() {
        return contractServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public Contract getContractById(@PathVariable Long id) {
        return contractServiceImpl.findById(id);
    }

    @PostMapping("/add")
    public Contract createContract(@RequestParam("vehicleID") Long vehicleId,
                                   @RequestParam("customerFullName") String customerFullName,
                                   @RequestParam("pickDate") @DateTimeFormat(pattern = "MM-dd-yyyy") Date pickDateReservation,
                                   @RequestParam("returnDate") @DateTimeFormat(pattern = "MM-dd-yyyy") Date returnDateReservation)
            throws VehicleNotFoundException, UserNotFoundException {

        return contractServiceImpl.createContract(vehicleId, customerFullName, pickDateReservation,
                returnDateReservation);
    }

    @DeleteMapping("/{id}")
    public void deleteContract(@PathVariable Long id) {
        contractServiceImpl.deleteById(id);
    }
}