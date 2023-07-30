package com.ayenijeremiaho.rovaaccount.util;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.account.model.CurrentAccount;
import com.ayenijeremiaho.rovaaccount.account.model.SavingsAccount;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;
import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GeneralTestUtil {

    public static final String FIRSTNAME = "jay";
    public static final String SURNAME = "jeremiah";

    public static Account getCurrentAccount(Customer customer) {
        CurrentAccount account = new CurrentAccount();
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ONE);
        account.setCreationDate(LocalDateTime.now());
        account.setAccountNo(12345L);
        return account;
    }

    public static Account getSavingsAccount(Customer customer) {
        SavingsAccount account = new SavingsAccount();
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ONE);
        account.setCreationDate(LocalDateTime.now());
        account.setAccountNo(12345L);
        return account;
    }

    public static Transaction getTransaction(Account account) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setBalanceBefore(BigDecimal.ZERO);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccount(account);
        transaction.setBalanceAfter(BigDecimal.ONE);

        return transaction;
    }

    public static Customer getCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname(FIRSTNAME);
        customer.setSurname(SURNAME);
        return customer;
    }

}
