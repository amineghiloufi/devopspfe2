package com.example.demoo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Contravention")
public class Contravention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long contraventionId;

    @Column
    private String customerFullName;

    @Column
    private Date contraventionDate;

    @Column
    private String contraventionDescription;

    @Column
    private boolean active;

    @ManyToOne
    @JsonIgnore
    private DefUser user;

    public Contravention() {

    }

    public Contravention(Long contraventionId, DefUser user, String customerFullName,
                         Date contraventionDate, String contraventionDescription, boolean active) {
        this.contraventionId = contraventionId;
        this.user = user;
        this.customerFullName = customerFullName;
        this.contraventionDate = contraventionDate;
        this.contraventionDescription = contraventionDescription;
        this.active = active;
    }

    public Long getContraventionId() {
        return contraventionId;
    }

    public void setContraventionId(Long contraventionId) {
        this.contraventionId = contraventionId;
    }

    public DefUser getUser() {
        return user;
    }

    public void setUser(DefUser user) {
        this.user = user;
    }

    public String getCustomerFullName() { return customerFullName; }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public Date getContraventionDate() {
        return contraventionDate;
    }

    public void setContraventionDate(Date contraventionDate) {

        this.contraventionDate = contraventionDate;
    }

    public String getContraventionDescription() {
        return contraventionDescription;
    }

    public void setContraventionDescription(String contraventionDescription) {
        this.contraventionDescription = contraventionDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}