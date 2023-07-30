package com.ayenijeremiaho.rovaaccount.account.model;

import com.ayenijeremiaho.rovaaccount.account.enums.AccountType;
import com.ayenijeremiaho.rovaaccount.customer.model.Customer;
import com.ayenijeremiaho.rovaaccount.transaction.model.Transaction;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "accountType",
        discriminatorType = DiscriminatorType.STRING)
public class Account {

    @Id
    private Long accountNo;

    private BigDecimal balance = new BigDecimal("0.00");

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Transaction> transactions;

    private LocalDateTime creationDate = LocalDateTime.now();

    @Transient
    public String getDiscriminatorValue(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );

        return val == null ? null : val.value();
    }
}
