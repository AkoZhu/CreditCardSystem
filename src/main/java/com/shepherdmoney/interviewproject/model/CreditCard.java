package com.shepherdmoney.interviewproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "CREDIT_CARD")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "ISSUANCE_BANK")
    private String issuanceBank;

    @Column(name = "NUMBER", unique = true, nullable = false)
    private String number;

    //  Credit card's owner. For detailed hint, please see User class
    @ManyToOne
    @JoinColumn(name="USER_ID",  referencedColumnName = "ID")
    private User user;

    // TODO: Credit card's balance history. It is a requirement that the dates in the balanceHistory 
    //       list must be in chronological order, with the most recent date appearing first in the list. 
    //       Additionally, the first object in the list must have a date value that matches today's date, 
    //       since it represents the current balance of the credit card. For example:
    //       [
    //         {date: '2023-04-13', balance: 1500},
    //         {date: '2023-04-12', balance: 1200},
    //         {date: '2023-04-11', balance: 1000},
    //         {date: '2023-04-10', balance: 800}
    //       ]

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL)
    private List<BalanceHistory> balanceHistoryList = new ArrayList<>();

    public void addBalanceHistory(BalanceHistory balanceHistory) {
        // Add balance history to the list
        balanceHistoryList.add(balanceHistory);
        // Set the credit card
        balanceHistory.setCreditCard(this);
    }
}
