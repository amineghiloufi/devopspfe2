package com.example.demoo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity(name = "DefVehicleModel")
@Table(name = "DefVehicleModel")
public class DefVehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @ManyToOne
    private DefVehicleCategory category;

    @ManyToOne
    private DefBrand brand;

    @OneToMany
    @JsonIgnore
    private List<DefVehicle> vehicles;

    @Column
    private String name;

    @Column
    private String version;

    @Column
    private String year;

    @Column
    private String horsePower;

    @Column
    private Boolean active;

    public DefVehicleModel() {
    }

    public DefVehicleModel(Long modelId, DefVehicleCategory category, DefBrand brand,
                           List<DefVehicle> vehicles, String name, String version, String year,
                           String horsePower, Boolean active) {
        this.modelId = modelId;
        this.category = category;
        this.brand = brand;
        this.vehicles = vehicles;
        this.name = name;
        this.version = version;
        this.year = year;
        this.horsePower = horsePower;
        this.active = active;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public DefVehicleCategory getCategory() {
        return category;
    }

    public void setCategory(DefVehicleCategory category) {
        this.category = category;
    }

    public DefBrand getBrand() {
        return brand;
    }

    public void setBrand(DefBrand brand) {
        this.brand = brand;
    }

    public List<DefVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<DefVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(String horsePower) {
        this.horsePower = horsePower;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}