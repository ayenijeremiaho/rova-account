package com.ayenijeremiaho.rovaaccount.customer.dto;

import com.ayenijeremiaho.rovaaccount.account.dto.response.AccountDTO;
import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDTO extends CustomerBasicDTO {

    private List<AccountDTO> accounts;

    public static CustomerDTO get(Customer customer) {
        CustomerBasicDTO customerBasicDTO = CustomerBasicDTO.get(customer);

        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customerBasicDTO, customerDTO);

        List<Account> accounts = customer.getAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            List<AccountDTO> accountDTOS = customer.getAccounts().stream().map(AccountDTO::get).toList();
            customerDTO.setAccounts(accountDTOS);
        }

        return customerDTO;
    }
}
