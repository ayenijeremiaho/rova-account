package com.ayenijeremiaho.rovaaccount.account.repository;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
