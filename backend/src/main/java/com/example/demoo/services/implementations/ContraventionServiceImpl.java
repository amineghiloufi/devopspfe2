package com.example.demoo.services.implementations;

import com.example.demoo.domain.Contravention;
import com.example.demoo.domain.DefUser;
import com.example.demoo.repositories.ContraventionRepository;
import com.example.demoo.repositories.DefUserRepository;
import com.example.demoo.services.ContraventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ContraventionServiceImpl implements ContraventionService {

    private final ContraventionRepository contraventionRepository;

    private final DefUserRepository defUserRepository;

    @Autowired
    public ContraventionServiceImpl(ContraventionRepository contraventionRepository,
                                    DefUserRepository defUserRepository) {
        this.contraventionRepository = contraventionRepository;
        this.defUserRepository = defUserRepository;
    }

    @Override
    public List<Contravention> findAll() {
        return contraventionRepository.findAll();
    }

    @Override
    public Contravention findById(Long id) {

        return contraventionRepository.findById(id).orElse(null);
    }

    @Override
    public Contravention addContravention(String fullName, String contraventionDescription) {
        DefUser customer = defUserRepository.findByCustomerFullName(fullName);
        Contravention contravention = new Contravention();
        contravention.setUser(customer);
        contravention.setCustomerFullName(customer.getCustomerFullName());
        contravention.setContraventionDate(new Date());
        contravention.setContraventionDescription(contraventionDescription);
        contravention.setActive(true);
        customer.getContraventions().add(contravention);
        contraventionRepository.save(contravention);
        return contravention;
    }

    @Override
    public Contravention update(Long currentContraventionId, boolean active) {
        if (currentContraventionId != null) {
            Contravention currentContravention = findById(currentContraventionId);
            if (currentContravention != null) {
                currentContravention.setContraventionDate(new Date());
                currentContravention.setActive(active);
                contraventionRepository.save(currentContravention);
                return currentContravention;
            }
            return null;
        }
        return null;
    }

    @Override
    public void deleteById(Long contraventionId) {
        Optional<Contravention> contraventionOptional = contraventionRepository.findById(contraventionId);
        if (contraventionOptional.isPresent()) {
            Contravention contravention = contraventionOptional.get();
            DefUser customer = contravention.getUser();
            if (customer != null) {
                customer.getContraventions().remove(contravention);
                defUserRepository.save(customer);
            }
            contraventionRepository.delete(contravention);
        }
    }
}