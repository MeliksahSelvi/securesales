package com.meliksah.securesales.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author mselvi
 * @Created 28.08.2023
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stats {

    private Long totalCustomers;
    private Long totalInvoices;
    private Double totalBilled;
}
