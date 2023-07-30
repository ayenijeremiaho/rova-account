package com.ayenijeremiaho.rovaaccount.account.repository;

import com.ayenijeremiaho.rovaaccount.account.model.SavingsAccount;
import org.springframework.data.repository.CrudRepository;

public interface SavingsAccountRepository extends CrudRepository<SavingsAccount, Long> {
}
