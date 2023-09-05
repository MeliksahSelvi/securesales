package com.meliksah.securesales.domain;

import com.meliksah.securesales.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Entity
@Getter
@Setter
@Table(name = "INVOICE")
public class Invoice extends BaseEntity {

    @SequenceGenerator(name = "Invoice", sequenceName = "INVOICE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "Invoice")
    @Column
    @Id
    private Long id;

    @Column
    private String invoiceNumber;

    @Column
    private String services;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column
    private String status;

    @Column
    private double total;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID",nullable = false,foreignKey = @ForeignKey(name = "FK_INVOICE_CUSTOMER"))
    @JsonIgnore
    private Customer customer;
}
