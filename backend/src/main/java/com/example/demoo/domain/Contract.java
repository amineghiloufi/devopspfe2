package com.example.demoo.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long contractId;

    @ManyToOne
    @JsonIgnore
    private DefUser user;

    @Column
    private String agencyName;

    @Column
    private String customerFullName;

    @OneToOne
    @JsonIgnore
    private DefVehicle vehicle;

    @Column
    private Long vehicleID;

    @Column
    private String vehicleDescription;

    @OneToOne
    @JsonIgnore
    private Invoice invoice;

    @Column
    private Date pickDateReservation;

    @Column
    private Date returnDateReservation;

    @Column
    private Date creationDate;

    @Column
    private Float price;

    @Column
    private boolean isSignedByAgency;

    @Column
    private boolean isSignedByCustomer;

    @Column
    private boolean active;


    public Contract() {}

    public Contract(Long contractId, DefUser user, String agencyName, String customerFullName,
                    DefVehicle vehicle, Long vehicleID, String vehicleDescription, Invoice invoice,
                    Date pickDateReservation, Date returnDateReservation, Date creationDate,
                    Float price, boolean isSignedByAgency, boolean isSignedByCustomer,
                    boolean active) {
        this.contractId = contractId;
        this.user = user;
        this.agencyName = agencyName;
        this.customerFullName = customerFullName;
        this.vehicle = vehicle;
        this.vehicleID = vehicleID;
        this.vehicleDescription = vehicleDescription;
        this.invoice = invoice;
        this.pickDateReservation = pickDateReservation;
        this.returnDateReservation = returnDateReservation;
        this.creationDate = creationDate;
        this.price = price;
        this.isSignedByAgency = isSignedByAgency;
        this.isSignedByCustomer = isSignedByCustomer;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public DefUser getUser() {
        return user;
    }

    public void setUser(DefUser user) {
        this.user = user;
    }

    public String getAgencyName() { return agencyName; }

    public void setAgencyName(String agencyName) { this.agencyName = agencyName; }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public DefVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(DefVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Long getVehicleID() { return vehicleID; }

    public void setVehicleID(Long vehicleID) { this.vehicleID = vehicleID; }

    public String getVehicleDescription() { return vehicleDescription; }

    public void setVehicleDescription(String vehicleDescription) {
        this.vehicleDescription = vehicleDescription;
    }

    public Invoice getInvoice() { return invoice; }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Date getPickDateReservation() {
        return pickDateReservation;
    }

    public void setPickDateReservation(Date pickDateReservation) {
        this.pickDateReservation = pickDateReservation;
    }

    public Date getReturnDateReservation() {
        return returnDateReservation;
    }

    public void setReturnDateReservation(Date returnDateReservation) {
        this.returnDateReservation = returnDateReservation;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public boolean isSignedByAgency() {
        return isSignedByAgency;
    }

    public void setSignedByAgency(boolean signedByAgency) {
        isSignedByAgency = signedByAgency;
    }

    public boolean isSignedByCustomer() {
        return isSignedByCustomer;
    }

    public void setSignedByCustomer(boolean signedByCustomer) {
        isSignedByCustomer = signedByCustomer;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}