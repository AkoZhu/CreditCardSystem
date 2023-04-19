package com.shepherdmoney.interviewproject.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "MY_USER")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name= "EMAIL")
    private String email;

    //  User's credit card
    // HINT: A user can have one or more, or none at all. We want to be able to query credit cards by user
    //       and user by a credit card.

    // Assume a credit card needs to have a user. -> orphanRemoval = true
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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
