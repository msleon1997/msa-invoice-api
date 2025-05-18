package com.invoice.api.services;

import com.invoice.api.domain.InvoiceDetail;
import com.invoice.api.domain.InvoiceHeader;

import java.util.List;

public interface InvoiceService {
    InvoiceHeader create(InvoiceHeader invoiceHeader);
    InvoiceDetail createInvoiceDetail(InvoiceDetail invoiceDetail);

    InvoiceHeader findById(Integer id);
    InvoiceDetail findByIdDetail(Integer id);
    InvoiceHeader findByNumber(String number);
    InvoiceHeader update(InvoiceHeader invoiceHeader, Integer id);
    InvoiceDetail update(InvoiceDetail invoiceDetail, Integer id);

    List<InvoiceHeader> getAll();
    List<InvoiceDetail> getAllDetails();

    void deleteById(Integer id);
    void  deleteByIdDetail(Integer id);
    InvoiceHeader updateInvoiceByDate(InvoiceHeader invoiceHeader, Integer id);

}


