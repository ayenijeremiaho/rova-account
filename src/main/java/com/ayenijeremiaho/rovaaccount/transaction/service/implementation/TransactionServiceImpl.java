package com.ayenijeremiaho.rovaaccount.transaction.service.implementation;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.exception.GeneralException;
import com.ayenijeremiaho.rovaaccount.exception.NotFoundException;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionDTO;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionListDTO;
import com.ayenijeremiaho.rovaaccount.transaction.enums.TransactionType;
import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import com.ayenijeremiaho.rovaaccount.transaction.repository.TransactionRepository;
import com.ayenijeremiaho.rovaaccount.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void logTransaction(Account account, TransactionType transactionType, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        log.info("Saving transaction for {}", account.getAccountNo());

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType(transactionType);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);

        transactionRepository.save(transaction);
    }

    @Override
    public TransactionDTO getTransactionDTO(Long transactionId) {
        log.info("Getting transaction => {}", transactionId);

        Transaction transaction = getTransaction(transactionId);
        return TransactionDTO.get(transaction);
    }

    @Override
    public TransactionListDTO getAllTransactionsForAccount(Long accountNo, int size, int page) {

        if (page < 1) {
            throw new GeneralException("Page cannot be less than 1");
        }

        //page starts from 0, so reduce page by 1
        Pageable pageable = PageRequest.of(page - 1, size);

        //make request to retrieve transaction as a pageale
        Page<Transaction> transactionPage = transactionRepository.findByAccount_AccountNo(accountNo, pageable);

        //get transaction list dto
        return getTransactionListDTO(transactionPage);
    }

    private static TransactionListDTO getTransactionListDTO(Page<Transaction> transactionPage) {
        TransactionListDTO transactionListDTO = new TransactionListDTO();

        //check if page has content else ignore
        if (transactionPage.hasContent()) {
            List<TransactionDTO> transactionDTOS = transactionPage.get().map(TransactionDTO::get).toList();
            transactionListDTO.setTransactions(transactionDTOS);
            transactionListDTO.setTotalCount(transactionListDTO.getTotalCount());
            transactionListDTO.setHasNextRecord(transactionListDTO.isHasNextRecord());
        }

        return transactionListDTO;
    }

    private Transaction getTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

}
