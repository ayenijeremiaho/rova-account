package com.ayenijeremiaho.rovaaccount.transaction.service;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionDTO;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionListDTO;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;

import java.math.BigDecimal;

public interface TransactionService {

    void logTransaction(Account account, TransactionType transactionType, BigDecimal balanceBefore, BigDecimal balanceAfter);

    TransactionDTO getTransactionDTO(Long transactionId);

    TransactionListDTO getAllTransactionsForAccount(Long accountNo, int size, int page);
}
