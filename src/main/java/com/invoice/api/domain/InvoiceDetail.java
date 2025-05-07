package com.invoice.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "invoicedetail")
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String productName;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal subtotal;


    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private InvoiceHeader invoice;

    //calcular subtotal
    public void calculateSubtotal() {
        if (quantity != null && unitPrice != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }


    // Métodos de actualización
    public void update(InvoiceDetail invoiceDetail) {
        if (invoiceDetail == null) {
            throw new RuntimeException("Factura detalle no puede ser null");
        }

        updateIfDifferent(this::getQuantity, this::setQuantity, invoiceDetail.getQuantity());
        updateIfDifferent(this::getSubtotal, this::setSubtotal, invoiceDetail.getSubtotal());
        updateIfDifferent(this::getProductName, this::setProductName, invoiceDetail.getProductName());
        updateIfDifferent(this::getUnitPrice, this::setUnitPrice, invoiceDetail.getUnitPrice());
    }

    private <T> void updateIfDifferent(Supplier<T> getter, Consumer<T> setter, T newValue) {
        T currentValue = getter.get();
        if ((currentValue == null && newValue != null) ||
                (currentValue != null && !currentValue.equals(newValue))) {
            setter.accept(newValue);
        }
    }
}

