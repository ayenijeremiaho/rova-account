package com.ayenijeremiaho.rovaaccount.account.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@DiscriminatorValue("CURRENT")
@EqualsAndHashCode(callSuper = true)
public class CurrentAccount extends Account {

    private BigDecimal overDraft = new BigDecimal("0.00");

    private BigDecimal overDraftUsed = new BigDecimal("0.00");
}
