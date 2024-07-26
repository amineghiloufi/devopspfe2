package com.example.demoo.controllers;

import com.example.demoo.domain.Invoice;
import com.example.demoo.services.implementations.InvoiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceServiceImpl invoiceServiceImpl;

    @Autowired
    public InvoiceController(InvoiceServiceImpl invoiceServiceImpl) {
        this.invoiceServiceImpl = invoiceServiceImpl;
    }

    @GetMapping
    public List<Invoice> findAll() {
        return invoiceServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public Invoice findById(@PathVariable Long id) {
        return invoiceServiceImpl.findById(id);
    }

    @PostMapping("/add")
    public Invoice createInvoice(@RequestBody Invoice invoice, Long invoiceId) {
        return invoiceServiceImpl.createInvoice(invoiceId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        invoiceServiceImpl.deleteById(id);
    }
}