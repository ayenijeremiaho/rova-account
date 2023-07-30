package com.ayenijeremiaho.rovaaccount.customer.controller;

import com.ayenijeremiaho.rovaaccount.customer.dto.CustomerDTO;
import com.ayenijeremiaho.rovaaccount.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    @Operation(summary = "Retrieve all customer accounts", description = "Retrieves an array of accounts connected to the user")
    public ResponseEntity<CustomerDTO> getCustomerAccounts(@PathVariable Long customerId) {
        CustomerDTO customerDTO = customerService.getCustomerDTO(customerId);
        return ResponseEntity.ok(customerDTO);
    }
}
