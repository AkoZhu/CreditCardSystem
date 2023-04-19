package com.shepherdmoney.interviewproject;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.BalanceHistoryRepository;
import com.shepherdmoney.interviewproject.service.BalanceHistoryService;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestBalanceHistory {

    @Mock
    BalanceHistoryRepository balanceHistoryRepository;

    @InjectMocks
    BalanceHistoryService balanceHistoryService;

    @Mock
    private CreditCardService creditCardService;



    User testUser;
    CreditCard testCreditCard;

    BalanceHistory testBalanceHistory;


    @BeforeEach
    public void init() {
        testUser = new User();
        testUser.setName("testUser");
        testUser.setEmail("testUser@gmail.cpom");

        testCreditCard = new CreditCard();
        testCreditCard.setIssuanceBank("testBank");
        testCreditCard.setNumber("1234");

        testBalanceHistory = new BalanceHistory();
        testBalanceHistory.setBalance(100);
        testBalanceHistory.setDate(Instant.parse("2023-04-19T10:15:30Z"));


        testUser.addCreditCard(testCreditCard);
        testCreditCard.setUser(testUser);
    }


    @Test
    public void testAddBalanceHistoryToCreditCard(){
        when(creditCardService.getCreditCardByNumber(testCreditCard.getNumber())).thenReturn(testCreditCard);
        when(balanceHistoryRepository.save(testBalanceHistory)).thenReturn(testBalanceHistory);
        Assertions.assertEquals(testBalanceHistory, balanceHistoryService.addBalanceHistoryToCreditCard(testCreditCard.getNumber(), testBalanceHistory));
    }
}
