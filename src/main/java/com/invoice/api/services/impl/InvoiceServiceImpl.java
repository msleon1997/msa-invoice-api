package com.invoice.api.services.impl;

import com.invoice.api.domain.InvoiceDetail;
import com.invoice.api.domain.InvoiceHeader;
import com.invoice.api.exceptions.BadRequestException;
import com.invoice.api.exceptions.InternalServerErrorException;
import com.invoice.api.exceptions.NotContentException;
import com.invoice.api.exceptions.NotFoundException;
import com.invoice.api.repository.InvoiceDetailRepository;
import com.invoice.api.repository.InvoiceRepository;
import com.invoice.api.services.InvoiceService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceDetailRepository invoiceDetailRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Override
    public InvoiceHeader create(InvoiceHeader invoiceHeader) {
        try {
            if (invoiceHeader == null) {
                throw new BadRequestException("la cabecera de la factura no puede ser nulo.");
            }
            return invoiceRepository.save(invoiceHeader);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al crear la factura.", e);
        }
    }

    @Override
    public InvoiceDetail createInvoiceDetail(InvoiceDetail invoiceDetail) {
        try {
            invoiceDetail.calculateSubtotal();
            return invoiceDetailRepository.save(invoiceDetail);
        } catch (Exception e) {
            String errorMessage = "Error al crear la factura. Detalles: " + e.getMessage();
            e.printStackTrace();
            throw new InternalServerErrorException(errorMessage, e);
        }
    }



    @Override
    public InvoiceHeader findById(Integer id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura con ID " + id + " no encontrada."));
    }

    @Override
    public InvoiceDetail findByIdDetail(Integer id) {
        return invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura con ID " + id + " no encontrada."));
    }

    @Override
    public InvoiceHeader findByNumber(String number){
        return invoiceRepository.findByNumber(number)
                .orElseThrow(() -> new NotFoundException("Factura con el numero " + number + " no encontrada."));

    }




    @Override
    public InvoiceHeader update(InvoiceHeader invoiceHeader, Integer id) {
        if (invoiceHeader == null) {
            throw new BadRequestException("El cuerpo de la factura no puede ser nulo.");
        }
        InvoiceHeader invoiceToUpdate = this.findById(id);
        invoiceToUpdate.update(invoiceHeader);
        return invoiceRepository.save(invoiceToUpdate);
    }

    @Override
    public InvoiceDetail update(InvoiceDetail invoiceDetail, Integer id) {
        if (invoiceDetail == null) {
            throw new BadRequestException("El cuerpo de la factura no puede ser nulo.");
        }
        InvoiceDetail invoiceToUpdate = this.findByIdDetail(id);
        invoiceToUpdate.update(invoiceDetail);
        return invoiceDetailRepository.save(invoiceToUpdate);
    }

    @Override
    public List<InvoiceHeader> getAll() {
        List<InvoiceHeader> invoices = invoiceRepository.findAll();
        if (invoices.isEmpty()) {
            throw new NotContentException("No hay facturas registradas.");
        }
        return invoices;
    }

    @Override
    public List<InvoiceDetail> getAllDetails() {
        List<InvoiceDetail> invoicesDetails = invoiceDetailRepository.findAll();
        if (invoicesDetails.isEmpty()) {
            throw new NotContentException("No hay facturas registradas.");
        }
        return invoicesDetails;
    }

    @Override
    public void deleteById(Integer id) {
        this.findById(id); // Lanza NotFoundException si no existe
        try {
            invoiceRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al eliminar la factura.", e);
        }
    }

    @Override
    public void deleteByIdDetail(Integer id) {
        this.findById(id); // Lanza NotFoundException si no existe
        try {
            invoiceDetailRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al eliminar la factura Detalle.", e);
        }
    }

    @Override
    public InvoiceHeader updateInvoiceByDate(InvoiceHeader invoiceHeader, Integer id) {
        if (invoiceHeader == null || invoiceHeader.getDate() == null) {
            throw new BadRequestException("La fecha de la factura es requerida.");
        }
        InvoiceHeader invoiceToUpdate = this.findById(id);
        invoiceToUpdate.updateInvoiceData(invoiceHeader.getDate());
        return invoiceRepository.save(invoiceToUpdate);
    }

//    @Override
//    public Optional<InvoiceHeader> findInvoiceWithDetailsById(Long id) {
//        return Optional.empty();
//    }


}

