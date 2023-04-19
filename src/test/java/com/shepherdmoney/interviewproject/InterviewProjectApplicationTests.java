package com.shepherdmoney.interviewproject;

import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class InterviewProjectApplicationTests {

    @Test
    void contextLoads() {
    }


    @Mock
    CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardService creditCardService;
}
