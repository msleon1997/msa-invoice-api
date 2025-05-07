package com.invoice.api.controller;

import com.invoice.api.controller.api.InvoiceApi;
import com.invoice.api.controller.dto.InvoiceDto;
import com.invoice.api.domain.InvoiceDetail;
import com.invoice.api.domain.InvoiceHeader;
import com.invoice.api.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class InvoiceController implements InvoiceApi {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


//    @Override
//    public ResponseEntity<InvoiceHeader> getFullInvoice(@PathVariable Long id) {
//        Optional<InvoiceHeader> invoice = invoiceService.findInvoiceWithDetailsById(id);
//        return invoice.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }



    @Override
    public ResponseEntity<InvoiceHeader> createInvoiceHeader(InvoiceHeader invoiceheader) {
        InvoiceHeader savedInvoiceHeader = invoiceService.create(invoiceheader);
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
    public ResponseEntity<InvoiceHeader> findByInvoiceNumber(@PathVariable String invoiceNumber) {
        InvoiceHeader invoiceHeader = invoiceService.findByInvoiceNumber(invoiceNumber);
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
