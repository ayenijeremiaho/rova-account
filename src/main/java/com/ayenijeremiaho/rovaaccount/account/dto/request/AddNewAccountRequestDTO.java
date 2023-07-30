package com.ayenijeremiaho.rovaaccount.account.dto.request;

import com.ayenijeremiaho.rovaaccount.account.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddNewAccountRequestDTO {

    @NotNull(message = "An existing customer account must be provided")
    private Long customerId;

    @PositiveOrZero(message = "Initial Credit cannot be less than zero")
    private BigDecimal initialCredit;

    private AccountType accountType;
}
