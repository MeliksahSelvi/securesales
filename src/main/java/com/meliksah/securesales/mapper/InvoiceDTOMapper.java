package com.meliksah.securesales.mapper;

import com.meliksah.securesales.domain.Invoice;
import com.meliksah.securesales.dto.InvoiceDTO;
import org.springframework.beans.BeanUtils;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

public class InvoiceDTOMapper {

    public static InvoiceDTO fromInvoice(Invoice invoice) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        BeanUtils.copyProperties(invoice, invoiceDTO);
        return invoiceDTO;
    }

    public static Invoice fromDTO(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceDTO, invoice);
        return invoice;
    }

}
