package com.invoice.api.controller.api;

import com.invoice.api.controller.dto.InvoiceDto;
import com.invoice.api.domain.InvoiceDetail;
import com.invoice.api.domain.InvoiceHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/invoice")
@Validated
public interface InvoiceApi {
//    @GetMapping("/full/{id}")
//    ResponseEntity<InvoiceHeader> getFullInvoice(@PathVariable Long id);

    // ========== INVOICE HEADER ==========

    @PostMapping
    ResponseEntity<InvoiceHeader> createInvoiceHeader(@RequestBody InvoiceHeader invoiceHeader);

    @GetMapping
    ResponseEntity<List<InvoiceHeader>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<InvoiceHeader> findById(@PathVariable Long id);

    @GetMapping("/number/{invoiceNumber}")
    ResponseEntity<InvoiceHeader> findByInvoiceNumber(@PathVariable("invoiceNumber") String invoiceNumber);


    @PutMapping("/{id}")
    ResponseEntity<InvoiceHeader> update(@RequestBody InvoiceHeader invoiceHeader, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable Integer id);

    @PatchMapping("/{id}/date-invoice")
    ResponseEntity<InvoiceHeader> updateInvoiceHeaderByDate(@RequestBody InvoiceHeader date, @PathVariable Integer id);


    // ========== INVOICE DETAILS ==========
    @PostMapping("/details")
    ResponseEntity<InvoiceDetail> createInvoiceDetail(@RequestBody InvoiceDetail invoiceDetail);

    @GetMapping("/details")
    ResponseEntity<List<InvoiceDetail>> findAllDetail();

    @GetMapping("/details/{id}")
    ResponseEntity<InvoiceDetail> findByIdDetail(@PathVariable Long id);

    @PutMapping("/details/{id}")
    ResponseEntity<InvoiceDetail> updateDetail(@RequestBody InvoiceDetail invoiceDetail, @PathVariable Integer id);

    @DeleteMapping("/details/{id}")
    ResponseEntity<Void> deleteByIdDetail(@PathVariable Integer id);
}
