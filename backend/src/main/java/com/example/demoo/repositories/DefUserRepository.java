package com.example.demoo.repositories;

import com.example.demoo.domain.DefUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefUserRepository extends JpaRepository<DefUser, Long> {
    DefUser findDefUserByUsername(String username);
    DefUser findDefUserByEmail(String email);
    DefUser findByCustomerFullName(String customerFullName);

    DefUser findByAgencyName(String agencyName);
    Optional<DefUser> findAgencyByAgencyName(String agencyName);

    Optional<DefUser> findUserByCustomerFullName(String customerFullName);
}