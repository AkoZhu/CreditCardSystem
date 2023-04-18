package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.Exception.BusinessExceptionCode;
import com.shepherdmoney.interviewproject.Exception.BusinessException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.BalanceHistoryRepository;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

    private static final Logger LOG = LoggerFactory.getLogger(CreditCardService.class);

    @Resource
    CreditCardRepository creditCardRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    BalanceHistoryRepository balanceHistoryRepository;

    public CreditCard getCreditCardByUserAndNumber(User user, String number){
        return creditCardRepository.getCreditCardByUserAndNumber(user, number);
    }

    public CreditCard getCreditCardByUserIdAndNumber(int userId, String number){
        User user = userRepository.getUserById(userId);
        if(user == null){
            return null;
        }
        return getCreditCardByUserAndNumber(user, number);
    }


    public CreditCard getCreditCardById(int id){
        return creditCardRepository.findById(id).orElse(null);
    }

    public CreditCard getCreditCardByNumber(String number){
        return creditCardRepository.getCreditCardByNumber(number);
    }


    public CreditCard addCreditCard(String creditCardNumber, String creditCardIssuanceBank) throws BusinessException {
        if(getCreditCardByNumber(creditCardNumber) != null){
            throw new BusinessException(BusinessExceptionCode.CARD_ALREADY_EXIST);
        }
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber(creditCardNumber);
        creditCard.setIssuanceBank(creditCardIssuanceBank);
        return creditCardRepository.save(creditCard);
    }

    // No need to use deleteCreditCard since we use cascade delete and orphanRemoval
    public void deleteCreditCard(CreditCard creditCard) throws BusinessException {
        if(creditCard == null || getCreditCardById(creditCard.getId()) == null){
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
        }
        creditCardRepository.delete(creditCard);
    }


    public int addCreditCardToUser(int userId, String cardNumber, String cardIssuanceBank) throws BusinessException {

        try{
            User user = userRepository.getUserById(userId);
            if(user == null){
                throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND);
            }
            CreditCard creditCard = addCreditCard(cardNumber, cardIssuanceBank);
            user.addCreditCard(creditCard);

            return creditCard.getId();
        }catch (Exception e){
            LOG.error("Error in addCreditCardToUser: " + e.getMessage());
            throw new BusinessException(BusinessExceptionCode.CARD_ADD_FAILED);
        }
    }

    public void deleteCreditCardFromUser(int userId, String cardNumber) throws BusinessException {
        try{
            User user = userRepository.getUserById(userId);
            if(user == null){
                throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND);
            }
            CreditCard creditCard = getCreditCardByUserIdAndNumber(userId, cardNumber);
            if(creditCard == null){
                throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
            }
            user.removeCreditCard(creditCard);
        }catch (Exception e){
            LOG.error("Error in deleteCreditCardFromUser: " + e.getMessage());
            throw new BusinessException(BusinessExceptionCode.CARD_DELETE_FAILED);
        }
    }

    public int getUserIdForCreditCard(String cardNumber) throws BusinessException {
        CreditCard creditCard = getCreditCardByNumber(cardNumber);
        if (creditCard == null) {
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
        }
        return creditCard.getUser().getId();
    }

}
