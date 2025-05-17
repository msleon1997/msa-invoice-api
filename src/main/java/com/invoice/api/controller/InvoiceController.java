package com.invoice.api.controller;

import com.invoice.api.controller.api.InvoiceApi;
import com.invoice.api.domain.InvoiceDetail;
import com.invoice.api.domain.InvoiceHeader;
import com.invoice.api.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvoiceController implements InvoiceApi {

    private final InvoiceService invoiceService;
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @Override
    public ResponseEntity<InvoiceHeader> createInvoiceHeader(InvoiceHeader invoiceHeader) {

        if (invoiceHeader.getDetails() != null) {
            for (InvoiceDetail detail : invoiceHeader.getDetails()) {
                detail.setInvoice(invoiceHeader);
                detail.calculateSubtotal();
            }
        }

        //llamar a los metodos
        invoiceHeader.calculateSubtotalAmount();
        invoiceHeader.calculateVatAmount();
        invoiceHeader.calculateTotalAmount();
        InvoiceHeader savedInvoiceHeader = invoiceService.create(invoiceHeader);
        return new ResponseEntity<>(savedInvoiceHeader, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<List<InvoiceHeader>> findAll() {
        List<InvoiceHeader> invoiceHeaders = invoiceService.getAll();
        return new ResponseEntity<>(invoiceHeaders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvoiceHeader> findById(Long id) {
        InvoiceHeader invoiceHeader = invoiceService.findById(Math.toIntExact(id));
        return new ResponseEntity<>(invoiceHeader, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvoiceHeader> findByNumber(@PathVariable String number) {
        InvoiceHeader invoiceHeader = invoiceService.findByNumber(number);
        return new ResponseEntity<>(invoiceHeader, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<InvoiceHeader> update(InvoiceHeader invoiceHeader, Integer id) {
        InvoiceHeader updateInvoiceHeader = invoiceService.update(invoiceHeader, id);

        return new ResponseEntity<>(updateInvoiceHeader, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteById(Integer id) {
        invoiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<InvoiceHeader> updateInvoiceHeaderByDate(InvoiceHeader invoiceHeader, Integer id) {
        InvoiceHeader updatedInvoiceHeader = invoiceService.updateInvoiceByDate(invoiceHeader, id);
        return new ResponseEntity<>(updatedInvoiceHeader, HttpStatus.OK);
    }

    
    // metodos para invoice details
    @Override
    public ResponseEntity<InvoiceDetail> createInvoiceDetail(InvoiceDetail invoiceDetail) {
        invoiceDetail.calculateSubtotal();
        InvoiceDetail savedInvoiceDetail = invoiceService.createInvoiceDetail(invoiceDetail);

        // Lógica para recalcular montos del encabezado
        InvoiceHeader invoiceHeader = savedInvoiceDetail.getInvoice();
        invoiceHeader.calculateSubtotalAmount();
        invoiceHeader.calculateVatAmount();
        invoiceHeader.calculateTotalAmount();
        invoiceService.update(invoiceHeader, invoiceHeader.getId().intValue());

        return new ResponseEntity<>(savedInvoiceDetail, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<List<InvoiceDetail>> findAllDetail() {
        List<InvoiceDetail> invoiceDetails = invoiceService.getAllDetails();
        return new ResponseEntity<>(invoiceDetails, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvoiceDetail> findByIdDetail(Long id) {
        InvoiceDetail invoiceDetail = invoiceService.findByIdDetail(Math.toIntExact(id));
        return new ResponseEntity<>(invoiceDetail, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvoiceDetail> updateDetail(InvoiceDetail invoiceDetail, Integer id) {
        InvoiceDetail updateInvoiceDetail = invoiceService.update(invoiceDetail, id);
        return new ResponseEntity<>(updateInvoiceDetail, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteByIdDetail(Integer id) {
        invoiceService.deleteByIdDetail(id);
        return ResponseEntity.noContent().build();
    }
}
