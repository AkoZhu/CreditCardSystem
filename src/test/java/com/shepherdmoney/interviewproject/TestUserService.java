package com.shepherdmoney.interviewproject;


import com.shepherdmoney.interviewproject.Exception.BusinessException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.service.UserService;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestUserService {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    static User testUser;

    @BeforeEach
    public void init() {
        testUser = new User();
        testUser.setName("testUser");
        testUser.setEmail("testUser@gmail.com");
    }

    @DisplayName("Test createUser")
    @Test
    public void testCreateUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userRepository.getUserById(testUser.getId())).thenReturn(null);
        Assertions.assertEquals(testUser, userService.createUser(testUser));

        when(userRepository.getUserById(testUser.getId())).thenReturn(testUser);
        Assertions.assertThrows(BusinessException.class, () -> userService.createUser(testUser));
    }

    @DisplayName("Test getCreditCardByUserId")
    @Test
    public void testGetCreditCardByUserId(){
        when(userRepository.getUserById(testUser.getId())).thenReturn(testUser);
        Assertions.assertEquals(new ArrayList<>(), userService.getCreditCardsByUserId(testUser.getId()));

        CreditCard creditCard = new CreditCard();
        creditCard.setUser(testUser);
        creditCard.setNumber("1234");
        creditCard.setIssuanceBank("testBank");
        testUser.setCreditCardList(new ArrayList<>(){{add(creditCard);}});
        when(userRepository.getUserById(testUser.getId())).thenReturn(testUser);
        Assertions.assertEquals(testUser.getCreditCardList(), userService.getCreditCardsByUserId(testUser.getId()));

        when(userRepository.getUserById(testUser.getId())).thenReturn(null);
        Assertions.assertThrows(BusinessException.class, () -> userService.getCreditCardsByUserId(testUser.getId()));
    }

    @DisplayName("Test getAllCardOfUser")
    @Test
    public void testGetAllCardOfUser(){
        when(userRepository.getUserById(testUser.getId())).thenReturn(testUser);
        Assertions.assertEquals(new ArrayList<>(), userService.getAllCardOfUser(testUser.getId()));

        CreditCard creditCard = new CreditCard();
        creditCard.setUser(testUser);
        creditCard.setNumber("1234");
        creditCard.setIssuanceBank("testBank");

        CreditCardView creditCardView = CreditCardView
                .builder()
                .number(creditCard.getNumber())
                .issuanceBank(creditCard.getIssuanceBank())
                .build();
        testUser.setCreditCardList(new ArrayList<>(){{add(creditCard);}});
        when(userRepository.getUserById(testUser.getId())).thenReturn(testUser);
        Assertions.assertEquals(new ArrayList<>(){{add(creditCardView);}}, userService.getAllCardOfUser(testUser.getId()));

        when(userRepository.getUserById(testUser.getId())).thenReturn(null);
        Assertions.assertThrows(BusinessException.class, () -> userService.getAllCardOfUser(testUser.getId()));
    }





}
