package com.example.demoo.repositories;

import com.example.demoo.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByCustomerName(String customerName);
}