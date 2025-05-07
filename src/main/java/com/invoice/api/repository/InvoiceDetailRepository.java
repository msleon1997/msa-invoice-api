package com.invoice.api.repository;

import com.invoice.api.domain.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {
}
