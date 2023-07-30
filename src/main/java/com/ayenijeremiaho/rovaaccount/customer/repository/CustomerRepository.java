package com.ayenijeremiaho.rovaaccount.customer.repository;

import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
