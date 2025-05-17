package com.invoice.api.controller.dto;

import com.invoice.api.domain.InvoiceDetail;
import com.invoice.api.domain.InvoiceHeader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceDto_old {
    private InvoiceHeader invoiceHeader;
    private List<InvoiceDetail> invoiceDetails;

    public InvoiceHeader toEntity() {
        if (invoiceDetails != null) {
            for (InvoiceDetail detail : invoiceDetails) {
                detail.setInvoice(invoiceHeader);
            }
            invoiceHeader.setDetails(invoiceDetails);
        }
        return invoiceHeader;
    }
}



