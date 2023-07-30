package com.ayenijeremiaho.rovaaccount.customer.service.implementation;

import com.ayenijeremiaho.rovaaccount.customer.dto.CustomerDTO;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import com.ayenijeremiaho.rovaaccount.customer.repository.CustomerRepository;
import com.ayenijeremiaho.rovaaccount.customer.service.CustomerService;
import com.ayenijeremiaho.rovaaccount.exception.GeneralException;
import com.ayenijeremiaho.rovaaccount.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(String firstname, String surname) {
        log.info("Creating customer with details {} {}", firstname, surname);

        Customer customer = new Customer();
        customer.setFirstname(firstname);
        customer.setSurname(surname);

        customer = customerRepository.save(customer);
        return customer;
    }

    @Override
    public Customer retrieveCustomer(Long customerId) {
        return getCustomer(customerId);
    }

    @Override
    public CustomerDTO getCustomerDTO(Long customerId) {
        log.info("Getting customer full details incl. account {}", customerId);

        if (customerId == null) {
            throw new GeneralException("Invalid selection, customer not selected");
        }

        Customer customer = getCustomer(customerId);
        return CustomerDTO.get(customer);
    }

    private Customer getCustomer(Long customerId) {
        log.info("Getting customer => {}", customerId);

        return customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
    }
}
