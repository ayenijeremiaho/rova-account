package com.ayenijeremiaho.rovaaccount.account.dto.response;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import com.ayenijeremiaho.rovaaccount.customer.dto.CustomerBasicDTO;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerAccountDTO extends AccountDTO {

    private CustomerBasicDTO customerInfo;


    public static CustomerAccountDTO get(Account account) {
        AccountDTO accountDTO = AccountDTO.get(account);

        CustomerAccountDTO customerAccountDTO = new CustomerAccountDTO();
        BeanUtils.copyProperties(accountDTO, customerAccountDTO);

        Customer customer = account.getCustomer();
        CustomerBasicDTO customerBasicDTO = CustomerBasicDTO.get(customer);

        customerAccountDTO.setCustomerInfo(customerBasicDTO);

        return customerAccountDTO;
    }
}
