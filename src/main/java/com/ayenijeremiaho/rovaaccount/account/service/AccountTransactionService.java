package com.ayenijeremiaho.rovaaccount.account.service;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;

import java.math.BigDecimal;

public interface AccountTransactionService {

    Account performTransaction(Account account, BigDecimal amount, TransactionType transactionType);
}
