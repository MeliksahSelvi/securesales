package com.meliksah.securesales.service;

import com.meliksah.securesales.domain.*;
import com.meliksah.securesales.dto.ApiResponse;
import com.meliksah.securesales.dto.CustomerDTO;
import com.meliksah.securesales.dto.InvoiceDTO;
import com.meliksah.securesales.dto.UserDTO;
import com.meliksah.securesales.enumeration.ErrorMessage;
import com.meliksah.securesales.exception.ApiException;
import com.meliksah.securesales.exception.NotFoundException;
import com.meliksah.securesales.mapper.CustomerDTOMapper;
import com.meliksah.securesales.mapper.InvoiceDTOMapper;
import com.meliksah.securesales.mapper.UserDTOMapper;
import com.meliksah.securesales.report.CustomerReport;
import com.meliksah.securesales.repository.CustomerRepository;
import com.meliksah.securesales.repository.InvoiceRepository;
import com.meliksah.securesales.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final RoleService roleService;

    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_SIZE = 10;

    public Map<String, Object> getCustomersWithUser(Authentication authentication, Optional<Integer> page, Optional<Integer> size) {

        UserDTO userDTO = getUserDTO(authentication);

        List<Customer> customerList = findAllCustomerWithPagination(page, size);

        List<CustomerDTO> customerDTOList = convertCustomerListToDto(customerList);

        Map<String, Object> data = getUserCustomerListData(userDTO, customerDTOList);
        return data;
    }

    private UserDTO getUserDTO(Authentication authentication) {

        User user = UserUtil.getAuthenticatedUser(authentication);

        Role roleByUserId = roleService.getRoleByUserId(user.getId());

        UserDTO userDTO = UserDTOMapper.fromUser(user, roleByUserId);
        return userDTO;
    }

    private List<Customer> findAllCustomerWithPagination(Optional<Integer> page, Optional<Integer> size) {
        List<Customer> customerList = customerRepository.findAll(page.orElse(DEFAULT_PAGE), size.orElse(DEFAULT_SIZE));
        return customerList;
    }

    private List<CustomerDTO> convertCustomerListToDto(List<Customer> customerList) {
        return customerList.stream().map(CustomerDTOMapper::fromCustomer).collect(Collectors.toList());
    }

    private Map<String, Object> getUserCustomerListData(UserDTO userDTO, List<CustomerDTO> customerDTOList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("page", customerDTOList);
        dataMap.put("stats", getStats());
        return dataMap;
    }

    private Stats getStats() {
        Optional<Stats> statsOptional = customerRepository.getStats();

        return statsOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_STATS_FOUND);
        });
    }

    public ApiResponse createInvoice(Authentication authentication, Invoice invoice) {

        UserDTO userDTO = getUserDTO(authentication);

        invoice = saveInvoice(invoice);

        Map<String, Object> invoiceData = getUserInvoiceData(userDTO, invoice);

        return ApiResponse.of(HttpStatus.CREATED, invoiceData, "Invoice Created");
    }

    private Invoice saveInvoice(Invoice invoice) {
        invoice.setInvoiceNumber(RandomStringUtils.randomAlphabetic(8).toUpperCase());
        return invoiceRepository.save(invoice);
    }

    private Map<String, Object> getUserInvoiceData(UserDTO userDTO, Invoice invoice) {
        InvoiceDTO invoiceDTO = InvoiceDTOMapper.fromInvoice(invoice);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("invoice", invoiceDTO);
        dataMap.put("customer", invoice.getCustomer());
        return dataMap;
    }

    public Map<String, Object> getInvoices(Authentication authentication, Optional<Integer> page, Optional<Integer> size) {

        UserDTO userDTO = getUserDTO(authentication);

        List<Invoice> invoiceList = invoiceRepository.findAll(page.orElse(DEFAULT_PAGE), size.orElse(DEFAULT_SIZE));

        List<InvoiceDTO> invoiceDTOList = convertInvoicesListToDto(invoiceList);

        Map<String, Object> data = getUserInvoiceListData(userDTO, invoiceDTOList);

        return data;
    }

    private Map<String, Object> getUserInvoiceListData(UserDTO userDTO, List<InvoiceDTO> invoiceDTOList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("page", invoiceDTOList);
        return dataMap;
    }

    private List<InvoiceDTO> convertInvoicesListToDto(List<Invoice> invoiceList) {
        return invoiceList.stream().map(InvoiceDTOMapper::fromInvoice).collect(Collectors.toList());
    }

    public Map<String, Object> addInvoiceToCustomer(Authentication authentication, Long customerId, Invoice invoice) {
        Customer customer = findCustomerById(customerId);

        invoice.setCustomer(customer);
        invoice = invoiceRepository.save(invoice);

        UserDTO userDTO = getUserDTO(authentication);

        Map<String, Object> data = getUserCustomerInvoiceData(userDTO, customer, invoice);

        return data;
    }

    private Map<String, Object> getUserCustomerInvoiceData(UserDTO userDTO, Customer customer, Invoice invoice) {
        CustomerDTO customerDTO = CustomerDTOMapper.fromCustomer(customer);
        InvoiceDTO invoiceDTO = InvoiceDTOMapper.fromInvoice(invoice);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("customer", customerDTO);
        dataMap.put("invoice", invoiceDTO);
        return dataMap;
    }

    private Invoice getInvoiceById(Long invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);

        return invoiceOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_INVOICE_FOUND_BY_ID);
        });
    }


    public ApiResponse createCustomer(Authentication authentication, Customer customer) {

        UserDTO userDTO = getUserDTO(authentication);

        checkCustomerEmailIsUnique(userDTO.getEmail());

        customer = saveCustomer(customer);

        Map<String, Object> customerData = getUserCustomerData(userDTO, customer);

        return ApiResponse.of(HttpStatus.CREATED, customerData, "Customer Created");
    }

    private void checkCustomerEmailIsUnique(String email) {

        Optional<Customer> optional = customerRepository.getCustomerByEmail(email);
        optional.ifPresent(customer -> {
            throw new ApiException(ErrorMessage.EMAIL_ALREADY_IN_USE);
        });
    }

    private Customer saveCustomer(Customer customer) {
        customer.setCreatedAt(new Date());
        return customerRepository.save(customer);
    }

    private Map<String, Object> getUserCustomerData(UserDTO userDTO, Customer customer) {
        CustomerDTO customerDTO = CustomerDTOMapper.fromCustomer(customer);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userDTO);
        dataMap.put("customer", customerDTO);
        return dataMap;
    }

    public Map<String, Object> getCustomerById(Authentication authentication, Long id) {
        Customer customer = findCustomerById(id);

        UserDTO userDTO = getUserDTO(authentication);

        Map<String, Object> data = getUserCustomerData(userDTO, customer);
        return data;
    }

    private Customer findCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_FOUND_CUSTOMER_BY_ID);
        });
    }


    public Map<String, Object> searchCustomer(Authentication authentication, Optional<String> name, Optional<Integer> page, Optional<Integer> size) {

        UserDTO userDTO = getUserDTO(authentication);

        List<Customer> customerList = findAllCustomerByNameWithPagination(name, page, size);

        List<CustomerDTO> customerDTOList = convertCustomerListToDto(customerList);

        Map<String, Object> data = getUserCustomerListData(userDTO, customerDTOList);

        return data;
    }

    private List<Customer> findAllCustomerByNameWithPagination(Optional<String> name, Optional<Integer> page, Optional<Integer> size) {
        List<Customer> customerList = customerRepository.findAllCustomerByName(name.orElse(""), page.orElse(DEFAULT_PAGE), size.orElse(DEFAULT_SIZE));
        return customerList;
    }

    public Map<String, Object> updateCustomerDetails(Authentication authentication, Customer customer) {

        UserDTO userDTO = getUserDTO(authentication);

        customer = updateCustomer(customer);

        Map<String, Object> data = getUserCustomerData(userDTO, customer);

        return data;
    }

    private Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Map<String, Object> newInvoice(Authentication authentication) {

        UserDTO userDTO = getUserDTO(authentication);

        List<Customer> customerList = getAllCustomers();

        List<CustomerDTO> customerDTOList = convertCustomerListToDto(customerList);

        Map<String, Object> responseData = getUserCustomerListData(userDTO, customerDTOList);

        return responseData;
    }

    private List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Map<String, Object> getInvoiceById(Authentication authentication, Long id) {
        Invoice invoice = findInvoiceById(id);

        UserDTO userDTO = getUserDTO(authentication);

        Map<String, Object> data = getUserInvoiceData(userDTO, invoice);
        return data;
    }

    private Invoice findInvoiceById(Long id) {
        Optional<Invoice> byId = invoiceRepository.findById(id);
        return byId.orElseThrow(() -> {
            throw new NotFoundException(ErrorMessage.NO_FOUND_INVOICE_BY_ID);
        });
    }

    public InputStreamResource downloadReport() {
        List<Customer> customerList = getAllCustomers();

        CustomerReport customerReport=new CustomerReport(customerList);
        return customerReport.export();
    }
}
