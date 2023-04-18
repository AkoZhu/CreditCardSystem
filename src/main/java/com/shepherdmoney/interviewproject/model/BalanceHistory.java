package com.shepherdmoney.interviewproject.model;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "BALANCE_HISTORY")
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "DATE")
    private Instant date;

    @Column(name = "BALANCE")
    private double balance;

    // No foreign key needed here, since we are not querying by credit card
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private CreditCard creditCard;
    
}
