package com.example.demoo.repositories;

import com.example.demoo.domain.DefBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DefBrandRepository extends JpaRepository<DefBrand, Long> {
    DefBrand findByBrandName(String brandName);

    Optional<DefBrand> findBrandByBrandName(String brandName);

    @Query("SELECT b FROM DefBrand b JOIN b.categories c WHERE b.brandName = :brandName " +
            "AND c.categoryName = :categoryName")
    DefBrand findByBrandNameAndCategoryName(@Param("brandName") String brandName,
                                            @Param("categoryName") String categoryName);
}