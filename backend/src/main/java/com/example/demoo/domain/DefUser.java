package com.example.demoo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "DefUser")
public class DefUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private Long userId;

    @OneToMany
    @JsonIgnore
    private List<Contract> contracts;

    @OneToMany
    @JsonIgnore
    private List<Contravention> contraventions;

    @OneToMany
    @JsonIgnore
    private List<DefVehicle> vehicles;

    private String customerFullName;

    private Date customerBirthDate;

    @Column
    private String agencyName;

    @Column
    private String agencyAddress;

    @Column
    private String agencyOpeningHours;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    private boolean isActive;

    @Column
    private boolean isNotLocked;

    @Column
    private Date lastLoginDate;

    @Column
    private Date lastLoginDateDisplay;

    @Column
    private Date joinDate;

    @Column
    private String role;

    @Column
    private String[] authorities;

    public DefUser(Long userId, String customerFullName, Date customerBirthDate, String agencyName,
                   String agencyAddress, String agencyOpeningHours, String username, String email,
                   String phoneNumber, String password, boolean isActive, boolean isNotLocked,
                   Date lastLoginDate, Date lastLoginDateDisplay, Date joinDate, String role,
                   String[] authorities, List<Contract> contracts,
                   List<Contravention> contraventions, List<DefVehicle> vehicles) {
        this.userId = userId;
        this.customerFullName = customerFullName;
        this.customerBirthDate = customerBirthDate;
        this.agencyName = agencyName;
        this.agencyAddress = agencyAddress;
        this.agencyOpeningHours = agencyOpeningHours;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.joinDate = joinDate;
        this.role = role;
        this.authorities = authorities;
        this.contracts = contracts;
        this.contraventions = contraventions;
        this.vehicles = vehicles;
    }

    public DefUser() {}

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getCustomerFullName() { return customerFullName; }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public Date getCustomerBirthDate() { return customerBirthDate; }

    public void setCustomerBirthDate(Date customerBirthDate) {
        this.customerBirthDate = customerBirthDate;
    }

    public String getAgencyName() { return agencyName; }

    public void setAgencyName(String agencyName) { this.agencyName = agencyName; }

    public String getAgencyAddress() { return agencyAddress; }

    public void setAgencyAddress(String agencyAddress) { this.agencyAddress = agencyAddress; }

    public String getAgencyOpeningHours() { return agencyOpeningHours; }

    public void setAgencyOpeningHours(String agencyOpeningHours) {
        this.agencyOpeningHours = agencyOpeningHours;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    public boolean isNotLocked() { return isNotLocked; }

    public void setNotLocked(boolean notLocked) { isNotLocked = notLocked; }

    public Date getLastLoginDate() { return lastLoginDate; }

    public void setLastLoginDate(Date lastLoginDate) { this.lastLoginDate = lastLoginDate; }

    public Date getLastLoginDateDisplay() { return lastLoginDateDisplay; }

    public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
        this.lastLoginDateDisplay = lastLoginDateDisplay;
    }

    public Date getJoinDate() { return joinDate; }

    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String[] getAuthorities() { return authorities; }

    public void setAuthorities(String[] authorities) { this.authorities = authorities; }

    public List<Contract> getContracts() { return contracts; }

    public void setContracts(List<Contract> contracts) { this.contracts = contracts; }

    public List<Contravention> getContraventions() { return contraventions; }

    public void setContravention(List<Contravention> contraventions) {
        this.contraventions = contraventions;
    }

    public void setContraventions(List<Contravention> contraventions) {
        this.contraventions = contraventions;
    }

    public List<DefVehicle> getVehicles() { return vehicles; }

    public void setVehicles(List<DefVehicle> vehicles) { this.vehicles = vehicles; }
}