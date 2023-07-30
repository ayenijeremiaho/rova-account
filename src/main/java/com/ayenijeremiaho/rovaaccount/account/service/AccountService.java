package com.ayenijeremiaho.rovaaccount.account.service;

import com.ayenijeremiaho.rovaaccount.account.dto.request.AddNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.request.CreateNewAccountRequestDTO;
import com.ayenijeremiaho.rovaaccount.account.dto.response.CustomerAccountDTO;

public interface AccountService {

    CustomerAccountDTO createNewAccount(CreateNewAccountRequestDTO accountRequestDTO);

    CustomerAccountDTO addNewAccount(AddNewAccountRequestDTO accountRequestDTO);

    CustomerAccountDTO getAccountDetails(Long accountNo);
}
