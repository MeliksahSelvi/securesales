package com.meliksah.securesales.domain;

import com.meliksah.securesales.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Entity
@Getter
@Setter
@Table(name = "CUSTOMER")
public class Customer extends BaseEntity {

    @SequenceGenerator(name = "Customer", sequenceName = "CUSTOMER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "Customer")
    @Column
    @Id
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column
    private String type;

    @Column
    private String status;

    @Column
    private String address;

    @Column(length = 30)
    private String phone;

    @Column
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "customer",fetch =FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Invoice> invoices;
}
