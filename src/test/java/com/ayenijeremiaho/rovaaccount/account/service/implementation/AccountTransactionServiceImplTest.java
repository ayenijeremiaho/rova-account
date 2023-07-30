package com.ayenijeremiaho.rovaaccount.account.service.implementation;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.account.repository.CurrentAccountRepository;
import com.ayenijeremiaho.rovaaccount.account.repository.SavingsAccountRepository;
import com.ayenijeremiaho.rovaaccount.account.service.AccountTransactionService;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;
import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import com.ayenijeremiaho.rovaaccount.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static com.ayenijeremiaho.rovaaccount.util.GeneralTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountTransactionServiceImplTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private SavingsAccountRepository savingsAccountRepository;

    @MockBean
    private CurrentAccountRepository currentAccountRepository;

    @Autowired
    private AccountTransactionService accountTransactionService;


    @Test
    void performTransactionTest() {
        Customer customer = getCustomer();
        Account expectedAccount = getCurrentAccount(customer);
        Account requestAccount = getRequestAccount(customer);

        Transaction transaction = getTransaction(expectedAccount);

        when(transactionRepository.save(any())).thenReturn(transaction);
        when(currentAccountRepository.save(any())).thenReturn(expectedAccount);

        Account account = accountTransactionService.performTransaction(requestAccount, BigDecimal.ONE, TransactionType.CREDIT);

        verify(savingsAccountRepository, times(0)).save(any());
        verify(currentAccountRepository, times(1)).save(any());

        assertEquals(account.getAccountNo(), expectedAccount.getAccountNo());
        assertEquals(account.getDiscriminatorValue(), expectedAccount.getDiscriminatorValue());
        assertEquals(account.getBalance(), expectedAccount.getBalance());
        assertEquals(account.getCustomer(), expectedAccount.getCustomer());
    }

    private static Account getRequestAccount(Customer customer) {
        Account requestAccount = getCurrentAccount(customer);
        requestAccount.setBalance(BigDecimal.ZERO);
        return requestAccount;
    }
}