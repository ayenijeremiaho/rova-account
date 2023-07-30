package com.ayenijeremiaho.rovaaccount.transaction.repository;

import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccount_AccountNo(Long account_accountNo, Pageable pageable);
}
