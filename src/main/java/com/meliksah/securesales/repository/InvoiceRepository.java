package com.meliksah.securesales.repository;

import com.meliksah.securesales.common.BaseRepository;
import com.meliksah.securesales.domain.Invoice;
import org.springframework.stereotype.Repository;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Repository
public class InvoiceRepository extends BaseRepository<Invoice, Long> {
    public InvoiceRepository() {
        super(Invoice.class);
    }
}
