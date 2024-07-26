package com.example.demoo.repositories;

import com.example.demoo.domain.Contravention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ContraventionRepository extends JpaRepository<Contravention, Long> {
    Contravention findByContraventionDate(Date contraventionDate);
    Contravention findByContraventionId(Long contraventionId);
    Contravention findByCustomerFullName(String customerFullName);
}