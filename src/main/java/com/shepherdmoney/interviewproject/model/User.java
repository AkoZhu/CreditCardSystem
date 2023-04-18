package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "MyUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    // TODO: User's credit card
    // HINT: A user can have one or more, or none at all. We want to be able to query credit cards by user
    //       and user by a credit card.

    // Assume a credit card needs to have a user. -> orphanRemoval = true
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCard> creditCardList = new ArrayList<>();


    public void addCreditCard(CreditCard creditCard) {
        // Add credit card to the list
        creditCardList.add(creditCard);
        // Set the user
        creditCard.setUser(this);
    }

    public void removeCreditCard(CreditCard creditCard){
        if(creditCard != null){
            creditCardList.remove(creditCard);
            creditCard.setUser(null);
        }
    }


}
