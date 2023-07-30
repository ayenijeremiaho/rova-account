package com.ayenijeremiaho.rovaaccount.account.dto.response;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.transaction.dto.TransactionDTO;
import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import com.ayenijeremiaho.rovaaccount.utility.AccountUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDTO {

    private Long accountNo;

    private BigDecimal balance;

    private String accountType;

    private List<TransactionDTO> transactions;

    private String creationDate;

    public static AccountDTO get(Account account) {
        AccountDTO accountDTO = new AccountDTO();

        BeanUtils.copyProperties(account, accountDTO);
        accountDTO.setAccountType(account.getDiscriminatorValue());
        accountDTO.setCreationDate(AccountUtil.getDateAsString(account.getCreationDate()));

        List<Transaction> transactions = account.getTransactions();
        if (transactions != null && !transactions.isEmpty()) {
            List<TransactionDTO> transactionDTOS = transactions.stream().map(TransactionDTO::get).toList();
            accountDTO.setTransactions(transactionDTOS);
        }

        return accountDTO;
    }
}
