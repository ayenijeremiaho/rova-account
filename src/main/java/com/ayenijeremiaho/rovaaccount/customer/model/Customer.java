package com.ayenijeremiaho.rovaaccount.customer.model;

import com.ayenijeremiaho.rovaaccount.account.model.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstname;

    @NotNull
    private String surname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Account> accounts;
}
