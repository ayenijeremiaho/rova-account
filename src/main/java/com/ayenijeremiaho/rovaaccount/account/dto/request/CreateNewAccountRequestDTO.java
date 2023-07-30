package com.ayenijeremiaho.rovaaccount.account.dto.request;

import com.ayenijeremiaho.rovaaccount.account.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateNewAccountRequestDTO {

    @NotBlank(message = "Firstname cannot be empty")
    private String firstname;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    private AccountType accountType;

    @PositiveOrZero(message = "Initial Credit cannot be less than zero")
    private BigDecimal initialCredit;
}
