package com.ayenijeremiaho.rovaaccount.account.service.implementation;

import com.ayenijeremiaho.rovaaccount.account.dto.request.AddNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.request.CreateNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.response.CustomerAccountDTO;
import com.ayenijeremiaho.rovaaccount.account.enums.AccountType;
import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.account.repository.AccountRepository;
import com.ayenijeremiaho.rovaaccount.account.repository.CurrentAccountRepository;
import com.ayenijeremiaho.rovaaccount.account.repository.SavingsAccountRepository;
import com.ayenijeremiaho.rovaaccount.account.service.AccountService;
import com.ayenijeremiaho.rovaaccount.account.service.AccountTransactionService;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import com.ayenijeremiaho.rovaaccount.customer.service.CustomerService;
import com.ayenijeremiaho.rovaaccount.exception.GeneralException;
import com.ayenijeremiaho.rovaaccount.exception.NotFoundException;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;
import com.ayenijeremiaho.rovaaccount.util.GeneralTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceImplTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private SavingsAccountRepository savingsAccountRepository;

    @MockBean
    private CurrentAccountRepository currentAccountRepository;

    @MockBean
    private AccountTransactionService accountTransactionService;

    @Test
    void createNewCurrentAccountTest() {
        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);

        CreateNewAccountRequestDTO requestDTO = new CreateNewAccountRequestDTO();
        requestDTO.setFirstname(customer.getFirstname());
        requestDTO.setSurname(customer.getSurname());
        requestDTO.setInitialCredit(BigDecimal.ONE);

        when(accountRepository.existsById(any())).thenReturn(false);
        when(customerService.createCustomer(requestDTO.getFirstname(), requestDTO.getSurname())).thenReturn(customer);
        when(currentAccountRepository.save(any())).thenReturn(account);
        when(accountTransactionService.performTransaction(account, requestDTO.getInitialCredit(), TransactionType.CREDIT)).thenReturn(account);

        CustomerAccountDTO accountDTO = accountService.createNewAccount(requestDTO);

        verify(accountRepository, times(1)).existsById(any());
        verify(currentAccountRepository, times(1)).save(any());
        verify(savingsAccountRepository, times(0)).save(any());
        verify(accountTransactionService, times(1)).performTransaction(any(), any(), any());

        Assertions.assertEquals(accountDTO.getAccountNo(), account.getAccountNo());
        Assertions.assertEquals(accountDTO.getBalance(), account.getBalance());
        Assertions.assertEquals(accountDTO.getAccountType(), account.getDiscriminatorValue());
    }

    @Test
    void createNewSavingsAccountTest() {
        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getSavingsAccount(customer);

        CreateNewAccountRequestDTO requestDTO = new CreateNewAccountRequestDTO();
        requestDTO.setFirstname(customer.getFirstname());
        requestDTO.setSurname(customer.getSurname());
        requestDTO.setInitialCredit(BigDecimal.ONE);
        requestDTO.setAccountType(AccountType.SAVINGS);

        when(accountRepository.existsById(any())).thenReturn(false);
        when(customerService.createCustomer(requestDTO.getFirstname(), requestDTO.getSurname())).thenReturn(customer);
        when(savingsAccountRepository.save(any())).thenReturn(account);
        when(accountTransactionService.performTransaction(account, requestDTO.getInitialCredit(), TransactionType.CREDIT)).thenReturn(account);

        CustomerAccountDTO accountDTO = accountService.createNewAccount(requestDTO);

        verify(accountRepository, times(1)).existsById(any());
        verify(currentAccountRepository, times(0)).save(any());
        verify(savingsAccountRepository, times(1)).save(any());

        //verify account credit is called since initial balance is > 0
        verify(accountTransactionService, times(1)).performTransaction(any(), any(), any());

        Assertions.assertEquals(accountDTO.getAccountNo(), account.getAccountNo());
        Assertions.assertEquals(accountDTO.getBalance(), account.getBalance());
        Assertions.assertEquals(accountDTO.getAccountType(), account.getDiscriminatorValue());
    }

    @Test
    void addNewAccountTestWithZeroInitialCreditTest() {
        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);

        AddNewAccountRequestDTO requestDTO = new AddNewAccountRequestDTO();
        requestDTO.setCustomerId(customer.getId());
        requestDTO.setInitialCredit(BigDecimal.ZERO);

        when(customerService.retrieveCustomer(any())).thenReturn(customer);
        when(accountRepository.existsById(any())).thenReturn(false);
        when(currentAccountRepository.save(any())).thenReturn(account);
        when(accountTransactionService.performTransaction(account, requestDTO.getInitialCredit(), TransactionType.CREDIT)).thenReturn(account);

        CustomerAccountDTO accountDTO = accountService.addNewAccount(requestDTO);

        verify(accountRepository, times(1)).existsById(any());
        verify(currentAccountRepository, times(1)).save(any());
        verify(savingsAccountRepository, times(0)).save(any());

        //verify account credit is NOT called since initial balance is = 0
        verify(accountTransactionService, times(0)).performTransaction(any(), any(), any());

        Assertions.assertEquals(accountDTO.getAccountNo(), account.getAccountNo());
        Assertions.assertEquals(accountDTO.getBalance(), account.getBalance());
        Assertions.assertEquals(accountDTO.getAccountType(), account.getDiscriminatorValue());
    }

    @Test
    void getAccountDetailsTest() {
        Customer customer = GeneralTestUtil.getCustomer();
        Account account = GeneralTestUtil.getCurrentAccount(customer);

        Optional<Account> optionalAccount = Optional.of(account);

        long id = 1L;
        when(accountRepository.findById(id)).thenReturn(optionalAccount);

        CustomerAccountDTO accountDetails = accountService.getAccountDetails(id);

        assertEquals(accountDetails.getAccountNo(), account.getAccountNo());
        assertEquals(accountDetails.getAccountType(), account.getDiscriminatorValue());
        assertEquals(accountDetails.getCustomerInfo().getId(), account.getCustomer().getId());
    }

    @Test
    void getInvalidAccountDetailsTest() {
        Optional<Account> invalidAccount = Optional.empty();

        long id = 1L;
        when(accountRepository.findById(id)).thenReturn(invalidAccount);

        assertThrows(NotFoundException.class, () -> accountService.getAccountDetails(id));
    }

    @Test
    void getInvalidIdAccountDetailsTest() {
        assertThrows(GeneralException.class, () -> accountService.getAccountDetails(null));
    }
}