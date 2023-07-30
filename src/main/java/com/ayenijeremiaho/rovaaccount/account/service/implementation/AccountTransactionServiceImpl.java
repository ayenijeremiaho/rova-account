package com.ayenijeremiaho.rovaaccount.account.service.implementation;

import com.ayenijeremiaho.rovaaccount.account.enums.AccountType;
import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.account.model.CurrentAccount;
import com.ayenijeremiaho.rovaaccount.account.model.SavingsAccount;
import com.ayenijeremiaho.rovaaccount.account.repository.CurrentAccountRepository;
import com.ayenijeremiaho.rovaaccount.account.repository.SavingsAccountRepository;
import com.ayenijeremiaho.rovaaccount.account.service.AccountTransactionService;
import com.ayenijeremiaho.rovaaccount.exception.GeneralException;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;
import com.ayenijeremiaho.rovaaccount.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountTransactionServiceImpl implements AccountTransactionService {

    private final TransactionService transactionService;
    private final SavingsAccountRepository savingsAccountRepository;
    private final CurrentAccountRepository currentAccountRepository;

    @Override
    @Synchronized
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Account performTransaction(Account account, BigDecimal amount, TransactionType transactionType) {
        log.info("Performing {} transaction on account {}", transactionType.name(), account.getAccountNo());

        //get current balance and prepare to save new balance
        BigDecimal balance = account.getBalance();
        BigDecimal newBalance;

        //check if it is debit or credit transaction using transaction type
        switch (transactionType) {
            case DEBIT -> {

                //verify account has sufficient balance
                if (balance.compareTo(amount) < 1) {
                    throw new GeneralException("Insufficient balance");
                }

                newBalance = balance.subtract(amount);
            }
            case CREDIT -> newBalance = balance.add(amount);
            default -> throw new GeneralException("Invalid input");
        }

        //use correct repository class to persist
        if (account.getDiscriminatorValue().equals(AccountType.CURRENT.name())) {
            CurrentAccount currentAccount = (CurrentAccount) account;
            account.setBalance(newBalance);
            account = currentAccountRepository.save(currentAccount);
        } else {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            account.setBalance(newBalance);
            account = savingsAccountRepository.save(savingsAccount);
        }

        transactionService.logTransaction(account, transactionType, balance, newBalance);

        return account;
    }
}
