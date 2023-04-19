package com.shepherdmoney.interviewproject;

import com.shepherdmoney.interviewproject.Exception.BusinessException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestCreditCardService {

    @Mock
    CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardService creditCardService;

    static CreditCard testCreditCard;
    static User testUser;

    @BeforeEach
    public void init() {
        testUser = new User();
        testUser.setName("testUser");
        testUser.setEmail("testUser@gmail.cpom");

        testCreditCard = new CreditCard();
        testCreditCard.setIssuanceBank("testBank");
        testCreditCard.setNumber("1234");

    }

    @DisplayName("Test addCreditCard")
    @Test
    public void testGetUserIdForCreditCard() {
       when(creditCardRepository.getCreditCardByNumber(testCreditCard.getNumber())).thenReturn(testCreditCard);
       testCreditCard.setUser(testUser);
       Assertions.assertEquals(testUser.getId(), creditCardService.getUserIdForCreditCard(testCreditCard.getNumber()));

       when(creditCardRepository.getCreditCardByNumber(testCreditCard.getNumber())).thenReturn(null);
       Assertions.assertThrows(BusinessException.class, () -> creditCardService.getUserIdForCreditCard(testCreditCard.getNumber()));
    }
}
