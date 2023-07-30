package com.ayenijeremiaho.rovaaccount.account.repository;

import com.ayenijeremiaho.rovaaccount.account.model.CurrentAccount;
import org.springframework.data.repository.CrudRepository;

public interface CurrentAccountRepository extends CrudRepository<CurrentAccount, Long> {
}
