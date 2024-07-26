package com.example.demoo.services;

import com.example.demoo.domain.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {

    List<Invoice> findAll();

    Invoice findById(Long id);

    Invoice createInvoice(Long contractId);

    void deleteById(Long id);
}
