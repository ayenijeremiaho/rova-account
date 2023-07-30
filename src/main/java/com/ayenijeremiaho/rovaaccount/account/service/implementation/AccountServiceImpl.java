package com.ayenijeremiaho.rovaaccount.account.service.implementation;

import com.ayenijeremiaho.rovaaccount.account.dto.request.AddNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.request.CreateNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.response.CustomerAccountDTO;
import com.ayenijeremiaho.rovaaccount.account.enums.AccountType;
import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.account.model.CurrentAccount;
import com.ayenijeremiaho.rovaaccount.account.model.SavingsAccount;
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
import com.ayenijeremiaho.rovaaccount.utility.AccountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final CustomerService customerService;
    private final AccountRepository accountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final CurrentAccountRepository currentAccountRepository;
    private final AccountTransactionService accountTransactionService;

    @Override
    @Transactional
    public CustomerAccountDTO createNewAccount(CreateNewAccountRequestDTO accountRequestDTO) {
        log.info("Creating new customer account {}", accountRequestDTO);

        //generate unique account number
        Long accountNumber = generateAccountNo();

        //create customer details
        Customer customer = customerService.createCustomer(accountRequestDTO.getFirstname(), accountRequestDTO.getSurname());

        AccountType accountType = accountRequestDTO.getAccountType();
        BigDecimal initialCredit = accountRequestDTO.getInitialCredit();

        return getAccountDTO(customer, accountNumber, accountType, initialCredit);
    }

    @Override
    public CustomerAccountDTO addNewAccount(AddNewAccountRequestDTO accountRequestDTO) {
        log.info("Adding new account to exiting customer {}", accountRequestDTO.getCustomerId());

        Customer customer = customerService.retrieveCustomer(accountRequestDTO.getCustomerId());

        //generate unique account number
        Long accountNumber = generateAccountNo();

        AccountType accountType = accountRequestDTO.getAccountType();
        BigDecimal initialCredit = accountRequestDTO.getInitialCredit();

        return getAccountDTO(customer, accountNumber, accountType, initialCredit);
    }

    @Override
    public CustomerAccountDTO getAccountDetails(Long accountNo) {
        log.info("Getting account details {}", accountNo);

        if (accountNo == null) {
            throw new GeneralException("Invalid selection, account not selected");
        }

        Account account = accountRepository.findById(accountNo).orElseThrow(() -> new NotFoundException("Account not found"));
        return CustomerAccountDTO.get(account);
    }


    private CustomerAccountDTO getAccountDTO(Customer customer, Long accountNumber, AccountType accountType, BigDecimal initialCredit) {
        //default account type to CURRENT if null
        if (accountType == null) {
            accountType = AccountType.CURRENT;
        }

        Account account = createAccount(customer, accountNumber, accountType);

        account = fundAccountIfNeeded(initialCredit, account);

        return CustomerAccountDTO.get(account);
    }

    private Account fundAccountIfNeeded(BigDecimal initialCredit, Account account) {
        //check if initial credit > 0, then perform transaction
        if (initialCredit.compareTo(BigDecimal.ZERO) > 0) {
            account = accountTransactionService.performTransaction(account, initialCredit, TransactionType.CREDIT);

        }
        return account;
    }

    private Account createAccount(Customer customer, Long accountNumber, AccountType accountType) {
        //create based on account type
        Account account;
        if (accountType.equals(AccountType.CURRENT)) {
            CurrentAccount currentAccount = new CurrentAccount();
            currentAccount.setAccountNo(accountNumber);
            currentAccount.setCustomer(customer);

            account = currentAccountRepository.save(currentAccount);
        } else {
            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setAccountNo(accountNumber);
            savingsAccount.setCustomer(customer);

            account = savingsAccountRepository.save(savingsAccount);
        }
        return account;
    }

    private Long generateAccountNo() {
        //recursive function to create a new account
        Long accountNo = AccountUtil.generateAccountNumber();

        if (accountRepository.existsById(accountNo)) {
            return generateAccountNo();
        }

        return accountNo;
    }
}
