package com.meliksah.securesales.mapper;

import com.meliksah.securesales.domain.Customer;
import com.meliksah.securesales.dto.CustomerDTO;
import org.springframework.beans.BeanUtils;

/**
 * @Author mselvi
 * @Created 10.08.2023
 */

public class CustomerDTOMapper {

    public static CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public static Customer fromDTO(CustomerDTO userDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(userDTO, customer);
        return customer;
    }

}
