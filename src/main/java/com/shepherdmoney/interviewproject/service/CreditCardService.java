package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.Exception.BusinessExceptionCode;
import com.shepherdmoney.interviewproject.Exception.BusinessException;
import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {

    @Resource
    CreditCardRepository creditCardRepository;

    @Resource
    UserRepository userRepository;


    public CreditCard getCreditCardById(int id){
        return creditCardRepository.getCreditCardById(id);
    }

    public CreditCard getCreditCardByNumber(String number){
        return creditCardRepository.getCreditCardByNumber(number);
    }

    // No need to use deleteCreditCard since we use cascade delete and orphanRemoval
    public void deleteCreditCard(CreditCard creditCard) throws BusinessException {
        if(creditCard == null || getCreditCardById(creditCard.getId()) == null){
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
        }
        creditCardRepository.delete(creditCard);
    }


    public int addCreditCardToUser(int userId, CreditCard creditCard) throws BusinessException {
        User user = userRepository.getUserById(userId);
        if(user == null){
            throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND);
        }
        user.addCreditCard(creditCard);

        creditCardRepository.save(creditCard);

        return creditCard.getId();
    }

    public void deleteCreditCardFromUser(int userId, String cardNumber) throws BusinessException {
        User user = userRepository.getUserById(userId);
        if(user == null){
            throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND);
        }
        CreditCard creditCard = getCreditCardByNumber(cardNumber);
        if(creditCard == null){
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
        }
        if(user.getId() != creditCard.getUser().getId()){
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_BELONG_TO_USER);
        }

        user.removeCreditCard(creditCard);
    }

    public int getUserIdForCreditCard(String cardNumber) throws BusinessException {
        CreditCard creditCard = getCreditCardByNumber(cardNumber);
        if (creditCard == null) {
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
        }
        return creditCard.getUser().getId();
    }


    public List<BalanceHistory> getBalanceHistoryListByCardNumber(String cardNumber) throws BusinessException {
        CreditCard creditCard = getCreditCardByNumber(cardNumber);
        if (creditCard == null) {
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
        }
        return creditCard.getBalanceHistoryList();
    }
}
