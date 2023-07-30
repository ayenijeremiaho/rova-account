package com.ayenijeremiaho.rovaaccount.transaction.model;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_no")
    private Account account;

    @Enumerated(EnumType.STRING)
    TransactionType transactionType;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private LocalDateTime transactionDate = LocalDateTime.now();
}
