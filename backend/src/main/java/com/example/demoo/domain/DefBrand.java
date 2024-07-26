package com.example.demoo.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "DefBrand")
@Table(name = "DefBrand")
public class DefBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @Column
    private String brandName;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DefVehicleModel> models;

    @ManyToMany
    private List<DefVehicleCategory> categories;

    public DefBrand(Long brandId, List<DefVehicleModel> models, List<DefVehicleCategory> categories,
                    String brandName) {
        this.brandId = brandId;
        this.models = models;
        this.categories = categories;
        this.brandName = brandName;
    }

    public DefBrand() { this.categories = new ArrayList<>(); }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public List<DefVehicleModel> getModels() {
        return models;
    }

    public void setModels(List<DefVehicleModel> models) {
        this.models = models;
    }

    public List<DefVehicleCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DefVehicleCategory> categories) {
        this.categories = categories;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}