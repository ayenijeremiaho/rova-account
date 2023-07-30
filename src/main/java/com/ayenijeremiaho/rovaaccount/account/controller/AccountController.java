package com.ayenijeremiaho.rovaaccount.account.controller;

import com.ayenijeremiaho.rovaaccount.account.dto.request.AddNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.request.CreateNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.response.CustomerAccountDTO;
import com.ayenijeremiaho.rovaaccount.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    @Operation(summary = "Creates account for a new customer", description = "This is for customers who do not previously have an account")
    public ResponseEntity<CustomerAccountDTO> createNewAccount(@Valid @RequestBody CreateNewAccountRequestDTO requestDTO) {
        CustomerAccountDTO accountDTO = accountService.createNewAccount(requestDTO);
        return ResponseEntity.ok(accountDTO);
    }

    @PostMapping("/add")
    @Operation(summary = "Creates account for an existing customer", description = "This is for customers who do already are a customer")
    public ResponseEntity<CustomerAccountDTO> addNewAccount(@Valid @RequestBody AddNewAccountRequestDTO requestDTO) {
        CustomerAccountDTO accountDTO = accountService.addNewAccount(requestDTO);
        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping("/{accountNo}")
    @Operation(summary = "Retrieve account details for one account number", description = "Retrieves an array of transactions connected to the account")
    public ResponseEntity<CustomerAccountDTO> getAccountDetail(@PathVariable Long accountNo) {
        CustomerAccountDTO customerDTO = accountService.getAccountDetails(accountNo);
        return ResponseEntity.ok(customerDTO);
    }
}
