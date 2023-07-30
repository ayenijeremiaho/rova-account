package com.ayenijeremiaho.rovaaccount.transaction.dto;

import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import com.ayenijeremiaho.rovaaccount.utility.AccountUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
public class TransactionDTO {

    private Long accountNo;

    private String transactionType;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private String transactionDate;

    public static TransactionDTO get(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        BeanUtils.copyProperties(transaction, transactionDTO);
        transactionDTO.setAccountNo(transaction.getAccount().getAccountNo());
        transactionDTO.setTransactionType(transaction.getTransactionType().name());
        transactionDTO.setTransactionDate(AccountUtil.getDateAsString(transaction.getTransactionDate()));
        return transactionDTO;
    }
}
