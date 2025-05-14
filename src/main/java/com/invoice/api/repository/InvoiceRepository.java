package com.invoice.api.repository;

import com.invoice.api.domain.InvoiceDetail;
import com.invoice.api.domain.InvoiceHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<InvoiceHeader, Integer> {
    Optional<InvoiceHeader> findByNumber(String number);
}
