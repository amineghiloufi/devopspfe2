package com.example.demoo.repositories;

import com.example.demoo.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    Contract findByContractId(Long contractId);

    Optional<Contract> findContractByContractId(Long contractId);
}