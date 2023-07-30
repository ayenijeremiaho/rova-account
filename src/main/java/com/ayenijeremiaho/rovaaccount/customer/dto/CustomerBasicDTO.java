package com.ayenijeremiaho.rovaaccount.customer.dto;

import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import lombok.Data;

@Data
public class CustomerBasicDTO {

    private Long id;

    private String firstname;

    private String surname;


    public static CustomerBasicDTO get(Customer customer) {
        CustomerBasicDTO customerBasicDTO = new CustomerBasicDTO();
        customerBasicDTO.setId(customer.getId());
        customerBasicDTO.setFirstname(customer.getFirstname());
        customerBasicDTO.setSurname(customer.getSurname());

        return customerBasicDTO;
    }
}
