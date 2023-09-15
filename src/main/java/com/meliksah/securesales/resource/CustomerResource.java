package com.meliksah.securesales.resource;

import com.meliksah.securesales.domain.Customer;
import com.meliksah.securesales.domain.Invoice;
import com.meliksah.securesales.dto.ApiResponse;
import com.meliksah.securesales.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 23.08.2023
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerResource {

    private final CustomerService customerService;

    @GetMapping("/list")
    public ApiResponse getCustomers(Authentication authentication, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        Map<String, Object> data = customerService.getCustomersWithUser(authentication, page, size);

        return ApiResponse.of(HttpStatus.OK, data, "Customers Retrieved");
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCustomer(Authentication authentication, @RequestBody Customer customer) {
        ApiResponse httpResponse = customerService.createCustomer(authentication, customer);

        return ResponseEntity.created(URI.create("")).body(httpResponse);
    }

    @GetMapping("/get/{id}")
    public ApiResponse getCustomerById(Authentication authentication, @PathVariable Long id) {
        Map<String, Object> data = customerService.getCustomerById(authentication, id);

        return ApiResponse.of(HttpStatus.OK, data, "Customer Found");
    }

    @GetMapping("/search")
    public ApiResponse searchCustomer(
            Authentication authentication,
            @RequestParam Optional<String> name,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        Map<String, Object> data = customerService.searchCustomer(authentication, name, page, size);

        return ApiResponse.of(HttpStatus.OK, data, "Customers Retrieved");
    }

    @PutMapping("/update")
    public ApiResponse updateCustomer(Authentication authentication, @RequestBody Customer customer) {
        Map<String, Object> data = customerService.updateCustomerDetails(authentication, customer);

        return ApiResponse.of(HttpStatus.OK, data, "Customer Updated");
    }

    @PostMapping("/invoice/create")
    public ResponseEntity<ApiResponse> createInvoice(Authentication authentication, @RequestBody Invoice invoice) {
        ApiResponse httpResponse = customerService.createInvoice(authentication, invoice);

        return ResponseEntity.created(URI.create("")).body(httpResponse);
    }

    @GetMapping("/invoice/new")
    public ApiResponse newInvoice(Authentication authentication) {
        Map<String, Object> data = customerService.newInvoice(authentication);

        return ApiResponse.of(HttpStatus.OK, data, "Customers Retrieved");
    }

    @GetMapping("/invoice/list")
    public ApiResponse getInvoices(Authentication authentication, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        Map<String, Object> data = customerService.getInvoices(authentication, page, size);

        return ApiResponse.of(HttpStatus.OK, data, "Invoices Retrieved");
    }

    @GetMapping("/invoice/get/{id}")
    public ApiResponse getInvoiceById(Authentication authentication, @PathVariable Long id) {
        Map<String, Object> data = customerService.getInvoiceById(authentication, id);

        return ApiResponse.of(HttpStatus.OK, data, "Invoice Found");
    }

    @PostMapping("/invoice/addtocustomer/{id}")
    public ApiResponse newInvoice(Authentication authentication, @PathVariable Long id, @RequestBody Invoice invoice) {
        Map<String, Object> data = customerService.addInvoiceToCustomer(authentication, id, invoice);

        return ApiResponse.of(HttpStatus.OK, data, String
                .format("Invoices Added To Customer With Id: %s", id));
    }

    @PostMapping("/download/report")
    public ResponseEntity<Resource> downloadReport() {
        InputStreamResource inputStreamResource = customerService.downloadReport();

        HttpHeaders headers = getHttpHeaders();

        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .headers(headers).body(inputStreamResource);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers=new HttpHeaders();
        headers.add("File-Name","customer-report-xlsx");
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment:File-Name=customer-report-xlsx");
        return headers;
    }
}
