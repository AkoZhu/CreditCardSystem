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


    public BalanceHistory addBalanceHistory(BalanceHistory balanceHistory){
        return balanceHistoryRepository.save(balanceHistory);
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
        return addBalanceHistory(balanceHistory);
    }

    public BalanceHistory addBalanceHistoryToCreditCard(String creditCardNumber, Instant date, double balance) throws BusinessException{
        BalanceHistory newBalanceHistory = new BalanceHistory();
        newBalanceHistory.setDate(date);
        newBalanceHistory.setBalance(balance);
        return addBalanceHistoryToCreditCard(creditCardNumber, newBalanceHistory);
    }

    public String[] addBalanceHistoryByPayloadList(UpdateBalancePayload[] payloads) throws BusinessException{
        String[] result = new String[payloads.length];
        for(int i = 0; i < payloads.length; i++){
            UpdateBalancePayload currentPayload = payloads[i];
            try{
                addBalanceHistoryToCreditCard(currentPayload.getCreditCardNumber(), currentPayload.getTransactionTime(), currentPayload.getCurrentBalance());
                result[i] = "Success:" + currentPayload.getCreditCardNumber() + ";" + currentPayload.getTransactionTime() + ";" + currentPayload.getCurrentBalance();
            }catch (BusinessException e){
                LOG.error("Error in addBalanceHistoryByPayloadList: " + e.getMessage());
                throw new BusinessException(BusinessExceptionCode.CARD_NOT_FOUND);
            }
        }
        return result;
    }
}
