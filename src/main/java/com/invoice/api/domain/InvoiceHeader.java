package com.invoice.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "invoiceheader")

public class InvoiceHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String invoiceNumber;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    @NotNull
    private String customerName;
    @NotNull
    private BigDecimal subtotalAmount;
    @NotNull
    private BigDecimal ivaAmount;
    @NotNull
    private BigDecimal totalAmount;
    @NotNull
    private int age;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvoiceDetail> details;



    public void update(InvoiceHeader invoiceHeader) {
        if (invoiceHeader == null) {
            throw new RuntimeException("Fecha a actualiziar no puede ser null");
        }

        updateIfDifferent(this::getInvoiceNumber, this::setInvoiceNumber, invoiceHeader.getInvoiceNumber());
        updateIfDifferent(this::getDate, this::setDate, invoiceHeader.getDate());
        updateIfDifferent(this::getCustomerName, this::setCustomerName, invoiceHeader.getCustomerName());
        updateIfDifferent(this::getSubtotalAmount, this::setSubtotalAmount, invoiceHeader.getSubtotalAmount());
        updateIfDifferent(this::getIvaAmount, this::setIvaAmount, invoiceHeader.getIvaAmount());
        updateIfDifferent(this::getTotalAmount, this::setTotalAmount, invoiceHeader.getTotalAmount());
        updateIfDifferent(this::getAge, this::setAge, invoiceHeader.getAge());
    }


    private <T> void updateIfDifferent(Supplier<T> getter, Consumer<T> setter, T newValue) {
        T currentValue = getter.get();
        if ((currentValue == null && newValue != null) ||
                (currentValue != null && !currentValue.equals(newValue))) {
            setter.accept(newValue);
        }
    }

    public void updateInvoiceData(LocalDateTime newDate) {
        if (newDate == null) {
            throw new RuntimeException("Invoice date not be null!!");
        }
        this.setDate(newDate);
    }

}
