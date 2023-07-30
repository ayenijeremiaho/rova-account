package com.ayenijeremiaho.rovaaccount.account.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@DiscriminatorValue("SAVINGS")
@EqualsAndHashCode(callSuper = true)
public class SavingsAccount extends Account {

    private BigDecimal minimumBalance = new BigDecimal("0.00");
}
