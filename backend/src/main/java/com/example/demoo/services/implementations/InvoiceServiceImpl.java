package com.example.demoo.services.implementations;

import com.example.demoo.domain.Contract;
import com.example.demoo.domain.Invoice;
import com.example.demoo.repositories.ContractRepository;
import com.example.demoo.repositories.DefUserRepository;
import com.example.demoo.repositories.InvoiceRepository;
import com.example.demoo.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ContractRepository contractRepository;
    private final DefUserRepository defUserRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              ContractRepository contractRepository,
                              DefUserRepository defUserRepository) {

        this.invoiceRepository = invoiceRepository;
        this.contractRepository = contractRepository;
        this.defUserRepository = defUserRepository;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public Invoice createInvoice(Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElse(null);
        if (contract != null) {
            Invoice invoice = new Invoice();
            invoice.setContract(contract);
            invoice.setCustomerName(contract.getUser().getCustomerFullName());
            invoice.setCreationDate(new Date());
            invoice.setVehicleDescription(contract.getVehicleDescription());
            Float price = contract.getPrice();
            String totalPayment = String .format("%.3f", price) + "$ + TAX";
            invoice.setTotalPayment(totalPayment);
            invoiceRepository.save(invoice);
            return invoice;
        }
        return null;
    }

    public void deleteById(Long id) {
        invoiceRepository.deleteById(id);
    }
}