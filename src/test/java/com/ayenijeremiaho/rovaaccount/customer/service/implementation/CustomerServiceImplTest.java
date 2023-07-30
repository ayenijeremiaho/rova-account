package com.ayenijeremiaho.rovaaccount.customer.service.implementation;

import com.ayenijeremiaho.rovaaccount.account.dto.response.AccountDTO;
import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.customer.dto.CustomerDTO;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import com.ayenijeremiaho.rovaaccount.customer.repository.CustomerRepository;
import com.ayenijeremiaho.rovaaccount.customer.service.CustomerService;
import com.ayenijeremiaho.rovaaccount.exception.GeneralException;
import com.ayenijeremiaho.rovaaccount.exception.NotFoundException;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionDTO;
import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static com.ayenijeremiaho.rovaaccount.util.GeneralTestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void createCustomerTest() {
        Customer customer = getCustomer();

        when(customerRepository.save(any())).thenReturn(customer);

        assertEquals(customerService.createCustomer(FIRSTNAME, SURNAME), customer);
    }

    @Test
    void retrieveCustomerTest() {
        Optional<Customer> optionalCustomer = Optional.of(getCustomer());

        long id = 1L;
        when(customerRepository.findById(id)).thenReturn(optionalCustomer);

        assertEquals(customerService.retrieveCustomer(id), optionalCustomer.get());
    }

    @Test
    void retrieveInvalidCustomerTest() {
        Optional<Customer> noCustomer = Optional.empty();

        long id = 1L;
        when(customerRepository.findById(id)).thenReturn(noCustomer);

        assertThrows(NotFoundException.class, () -> customerService.retrieveCustomer(id));
    }

    @Test
    void getCustomerDTOTest() {
        Customer customer = getCustomer();
        Account account = getCurrentAccount(customer);
        Transaction transaction = getTransaction(account);

        account.setTransactions(Collections.singletonList(transaction));
        customer.setAccounts(Collections.singletonList(account));

        Optional<Customer> optionalCustomer = Optional.of(customer);

        long id = 1L;
        when(customerRepository.findById(id)).thenReturn(optionalCustomer);

        CustomerDTO customerDTO = customerService.getCustomerDTO(id);
        assertEquals(customerDTO.getSurname(), SURNAME);
        assertEquals(customerDTO.getFirstname(), FIRSTNAME);

        //check if accounts is correctly parsed
        assertEquals(customerDTO.getAccounts().size(), 1);

        AccountDTO accountDTO = customerDTO.getAccounts().get(0);
        assertEquals(accountDTO.getAccountType(), "CURRENT");
        assertNotNull(accountDTO.getAccountNo());

        //check if transactions is correctly parsed
        assertEquals(accountDTO.getTransactions().size(), 1);

        TransactionDTO transactionDTO = accountDTO.getTransactions().get(0);
        assertNotNull(transactionDTO.getAccountNo());
        assertEquals(transactionDTO.getTransactionType(), "CREDIT");
        assertEquals(transactionDTO.getBalanceBefore(), BigDecimal.ZERO);
        assertEquals(transactionDTO.getBalanceAfter(), BigDecimal.ONE);
    }

    @Test
    void getNullCustomerDTOTest() {
        assertThrows(GeneralException.class, () -> customerService.getCustomerDTO(null));
    }

}