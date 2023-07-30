package com.ayenijeremiaho.rovaaccount.customer.service;

import com.ayenijeremiaho.rovaaccount.customer.dto.CustomerDTO;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;

public interface CustomerService {

    Customer createCustomer(String firstname, String surname);

    Customer retrieveCustomer(Long customerId);

    CustomerDTO getCustomerDTO(Long customerId);
}
