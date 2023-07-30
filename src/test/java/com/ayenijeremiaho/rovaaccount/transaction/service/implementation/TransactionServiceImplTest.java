package com.ayenijeremiaho.rovaaccount.transaction.service.implementation;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import com.ayenijeremiaho.rovaaccount.exception.GeneralException;
import com.ayenijeremiaho.rovaaccount.exception.NotFoundException;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionDTO;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionListDTO;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;
import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import com.ayenijeremiaho.rovaaccount.transaction.repository.TransactionRepository;
import com.ayenijeremiaho.rovaaccount.transaction.service.TransactionService;
import com.ayenijeremiaho.rovaaccount.util.GeneralTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void logTransactionTest() {
        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);
        Transaction transaction = GeneralTestUtil.getTransaction(account);

        when(transactionRepository.save(any())).thenReturn(transaction);

        transactionService.logTransaction(account, TransactionType.CREDIT, BigDecimal.ZERO, BigDecimal.ONE);

        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void getTransactionDTOTest() {
        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);
        Transaction transaction = GeneralTestUtil.getTransaction(account);
        Optional<Transaction> optionalTransaction = Optional.of(transaction);

        Long id = 1L;
        when(transactionRepository.findById(id)).thenReturn(optionalTransaction);

        TransactionDTO transactionDTO = transactionService.getTransactionDTO(id);

        assertEquals(transactionDTO.getTransactionType(), transaction.getTransactionType().name());
        assertEquals(transactionDTO.getAccountNo(), transaction.getAccount().getAccountNo());
        assertEquals(transactionDTO.getBalanceBefore(), transaction.getBalanceBefore());
        assertEquals(transactionDTO.getBalanceAfter(), transaction.getBalanceAfter());

        //balance after should be equal to account balance
        assertEquals(transactionDTO.getBalanceAfter(), account.getBalance());
    }

    @Test
    void getInvalidTransactionDTOTest() {
        Optional<Transaction> noTransaction = Optional.empty();

        Long id = 1L;
        when(transactionRepository.findById(id)).thenReturn(noTransaction);

        assertThrows(NotFoundException.class, () -> transactionService.getTransactionDTO(id));
    }

    @Test
    void getAllTransactionsForAccountTest() {
        int page = 1;
        int size = 10;

        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);
        Transaction transaction = GeneralTestUtil.getTransaction(account);
        List<Transaction> transactionList = Collections.singletonList(transaction);

        Long accountNo = account.getAccountNo();

        Pageable pageRequest = PageRequest.of(0, size);
        Page<Transaction> transactionPage = new PageImpl<>(transactionList, pageRequest, 1);
        List<TransactionDTO> transactionDTOS = transactionList.stream().map(TransactionDTO::get).toList();

        when(transactionRepository.findByAccount_AccountNo(accountNo, pageRequest)).thenReturn(transactionPage);

        TransactionListDTO transactionListDTO = transactionService.getAllTransactionsForAccount(accountNo, size, page);

        assertEquals(transactionListDTO.getTransactions(), transactionDTOS);
    }

    @Test
    void getAllTransactionsForAccountWithoutTransactionTest() {
        int page = 1;
        int size = 10;

        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);
        List<Transaction> noTransactionList = Collections.emptyList();

        Long accountNo = account.getAccountNo();

        Pageable pageRequest = PageRequest.of(0, size);
        Page<Transaction> emptyTransactionPage = new PageImpl<>(noTransactionList, pageRequest, 1);

        when(transactionRepository.findByAccount_AccountNo(accountNo, pageRequest)).thenReturn(emptyTransactionPage);

        TransactionListDTO transactionListDTO = transactionService.getAllTransactionsForAccount(accountNo, size, page);

        assertNull(transactionListDTO.getTransactions());
        assertEquals(transactionListDTO.getTotalCount(), 0);
        assertFalse(transactionListDTO.isHasNextRecord());
    }

    @Test
    void getAllTransactionsForInvalidPageRequestTest() {
        int page = 0;
        int size = 10;

        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);
        Long accountNo = account.getAccountNo();

        assertThrowsExactly(GeneralException.class, () -> transactionService.getAllTransactionsForAccount(accountNo, size, page), "Page cannot be less than 1");
    }
}