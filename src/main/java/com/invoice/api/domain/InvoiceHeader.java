package com.invoice.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.invoice.api.constants.Constant;
import com.invoice.api.domain.InvoiceDetail;
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
    private String number;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
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

    public void generateRandomInvoiceNumber() {
        String prefix = "INV-";
        int randomNumber = (int) (Math.random() * 90000) + 10000;
        this.number = prefix + randomNumber;
    }

    public void update(InvoiceHeader invoiceHeader) {
        if (invoiceHeader == null) {
            throw new RuntimeException("Fecha a actualiziar no puede ser null");
        }

        updateIfDifferent(this::getNumber, this::setNumber, invoiceHeader.getNumber());
        updateIfDifferent(this::getDate, this::setDate, invoiceHeader.getDate());
        updateIfDifferent(this::getCustomerName, this::setCustomerName, invoiceHeader.getCustomerName());
        updateIfDifferent(this::getSubtotalAmount, this::setSubtotalAmount, invoiceHeader.getSubtotalAmount());
        updateIfDifferent(this::getIvaAmount, this::setIvaAmount, invoiceHeader.getIvaAmount());
        updateIfDifferent(this::getTotalAmount, this::setTotalAmount, invoiceHeader.getTotalAmount());

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
