package com.example.demoo.services;

import com.example.demoo.domain.Contravention;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContraventionService {

    List<Contravention> findAll();

    Contravention findById(Long id);

    Contravention addContravention(String fullName, String contraventionDescription);

    Contravention update(Long currentContraventionId, boolean active);

    void deleteById(Long contraventionId);
}
