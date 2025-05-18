package com.invoice.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.invoice.api.constants.Constant;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private String number;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private String customerName;
    private BigDecimal subtotalAmount;
    private BigDecimal ivaAmount;
    private BigDecimal totalAmount;


    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvoiceDetail> details = new ArrayList<>();


    public void calculateSubtotalAmount(){
        subtotalAmount = BigDecimal.ZERO;
        for (InvoiceDetail invoiceDetail : details) {

            invoiceDetail.calculateSubtotal();
            subtotalAmount = subtotalAmount.add(invoiceDetail.getSubtotal());
        }

    }


    public void calculateVatAmount() {
        ivaAmount = subtotalAmount.multiply(Constant.VAT_RATE);
    }

    public void calculateTotalAmount() {
        totalAmount = subtotalAmount.add(ivaAmount);
    }

//    public void generateRandomInvoiceNumber() {
//        String prefix = "INV-";
//        int randomNumber = (int) (Math.random() * 90000) + 10000;
//        this.number = prefix + randomNumber;
//    }

    public void update(InvoiceHeader invoiceHeader) {
        if (invoiceHeader == null) {
            throw new RuntimeException("Factura a actualizar no puede ser null");
        }

        updateIfDifferent(this::getNumber, this::setNumber, invoiceHeader.getNumber());
        updateIfDifferent(this::getDate, this::setDate, invoiceHeader.getDate());
        updateIfDifferent(this::getCustomerName, this::setCustomerName, invoiceHeader.getCustomerName());

        if (invoiceHeader.getDetails() != null) {
            this.details.clear();
            for (InvoiceDetail detail : invoiceHeader.getDetails()) {
                detail.setInvoice(this);
                detail.calculateSubtotal();
                this.details.add(detail);
            }
        }

        this.calculateSubtotalAmount();
        this.calculateVatAmount();
        this.calculateTotalAmount();
    }


    private <T> void updateIfDifferent(Supplier<T> getter, Consumer<T> setter, T newValue) {
        T currentValue = getter.get();
        if ((currentValue == null && newValue != null) ||
                (currentValue != null && !currentValue.equals(newValue))) {
            setter.accept(newValue);
        }
    }

    public void updateInvoiceDate(LocalDate newDate) {
        if (newDate == null) {
            throw new RuntimeException("Invoice date not be null!!");
        }
        this.setDate(newDate);
    }



}
