package com.example.demoo.domain;

import javax.persistence.*;

@Entity(name = "DefVehicle")
@Table(name = "DefVehicle")
public class DefVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @ManyToOne
    private DefUser user;

    @ManyToOne
    private DefVehicleModel model;

    @OneToOne
    private Contract contract;

    @Column(unique = true)
    private String registrationNumber;

    @Column
    private String color;

    @Column
    private String description;

    @Column
    private  Float price;

    @Column
    private String vehicleStatus;

    public DefVehicle() {
    }

    public DefVehicle(Long vehicleId, DefUser user, DefVehicleModel model, Contract contract, String registrationNumber,
                      String color, String description, Float price, String vehicleStatus) {
        this.vehicleId = vehicleId;
        this.user = user;
        this.model = model;
        this.contract = contract;
        this.registrationNumber = registrationNumber;
        this.color = color;
        this.description = description;
        this.price = price;
        this.vehicleStatus = vehicleStatus;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public DefUser getUser() {
        return user;
    }

    public void setUser(DefUser user) {
        this.user = user;
    }

    public DefVehicleModel getModel() {
        return model;
    }

    public void setModel(DefVehicleModel model) {
        this.model = model;
    }

    public Contract getContract() { return contract; }

    public void setContract(Contract contract) { this.contract = contract; }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() { return price; }

    public void setPrice(Float price) { this.price = price; }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
}