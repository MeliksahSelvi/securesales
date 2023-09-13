package com.meliksah.securesales.dto;

import com.meliksah.securesales.domain.Invoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Author mselvi
 * @Created 25.08.2023
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String type;
    private String status;
    private String address;
    private String phone;
    private String imageUrl;
    private Date createdAt;
    private List<Invoice> invoices;
}
