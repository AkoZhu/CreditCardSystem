package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.Exception.BusinessExceptionCode;
import com.shepherdmoney.interviewproject.Exception.BusinessException;
import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.repository.BalanceHistoryRepository;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BalanceHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(BalanceHistoryService.class);

    @Resource
    BalanceHistoryRepository balanceHistoryRepository;

    @Resource
    CreditCardService creditCardService;


    public BalanceHistory getBalanceHistoryById(int id){
        return balanceHistoryRepository.findById(id).orElse(null);
    }

    public void deleteBalanceHistoryById(int id) throws Exception{
        if(balanceHistoryRepository.getBalanceHistoryById(id) == null){
            throw new BusinessException(BusinessExceptionCode.BALANCE_HISTORY_NOT_EXIST);
        }
        balanceHistoryRepository.deleteById(id);
    }

    public BalanceHistory addBalanceHistoryToCreditCard(String creditCardNumber, BalanceHistory balanceHistory) throws BusinessException {
        CreditCard creditCard = creditCardService.getCreditCardByNumber(creditCardNumber);
        if(creditCard == null){
            throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
        }

        creditCard.addBalanceHistory(balanceHistory);
        return balanceHistoryRepository.save(balanceHistory);
    }

}
