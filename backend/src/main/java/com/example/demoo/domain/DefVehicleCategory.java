package com.example.demoo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity(name = "DefVehicleCategory")
@Table(name = "DefVehicleCategory")
public class DefVehicleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToMany
    @JsonIgnore
    private List<DefBrand> brands;

    @OneToMany
    @JsonIgnore
    private List<DefVehicleModel> models;

    @Column(unique = true)
    private String categoryName;

    @Column
    private String categoryType;

    public DefVehicleCategory() {
    }

    public DefVehicleCategory(Long categoryId, List<DefBrand> brands, List<DefVehicleModel> models,
                              String categoryName, String categoryType) {
        this.categoryId = categoryId;
        this.brands = brands;
        this.models = models;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<DefBrand> getBrands() { return brands; }

    public void setBrands(List<DefBrand> brands) { this.brands = brands; }

    public List<DefVehicleModel> getModels() { return models; }

    public void setModels(List<DefVehicleModel> models) { this.models = models; }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}