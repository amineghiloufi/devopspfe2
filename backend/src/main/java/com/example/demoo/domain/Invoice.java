package com.example.demoo.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Invoice")
@Table(name = "Invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoiceId")
    private Long invoiceId;

    @OneToOne
    private Contract contract;

    @Column
    @GeneratedValue(generator = "customGenerator")
    @GenericGenerator(name = "customGenerator", strategy = "com.example.demoo.utility.customGenerator")
    private String reference;

    @Column
    private String customerName;

    @Column
    private Date creationDate;

    @Column
    private String vehicleDescription;

    @Column
    private String totalPayment;

    public Invoice() {
    }

    public Invoice(Long invoiceId, Contract contract, String reference, String customerName,
                   Date creationDate, String vehicleDescription, String totalPayment) {
        this.invoiceId = invoiceId;
        this.contract = contract;
        this.reference = reference;
        this.customerName = customerName;
        this.creationDate = creationDate;
        this.vehicleDescription = vehicleDescription;
        this.totalPayment = totalPayment;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCustomerName() { return customerName; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getVehicleDescription() { return vehicleDescription; }

    public void setVehicleDescription(String vehicleDescription) {
        this.vehicleDescription = vehicleDescription;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }
}