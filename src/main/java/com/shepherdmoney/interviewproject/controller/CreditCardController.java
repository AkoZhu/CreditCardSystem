package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.Exception.BusinessException;
import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.service.BalanceHistoryService;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import com.shepherdmoney.interviewproject.service.UserService;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CreditCardController {

    // wire in CreditCard repository here (~1 line)
    private static final Logger LOG = LoggerFactory.getLogger(CreditCardController.class);

    @Resource
    CreditCardService creditCardService;

    @Resource
    UserService userService;

    @Resource
    BalanceHistoryService balanceHistoryService;

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        //       Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length

        try {
            String creditCardNum = payload.getCardNumber();
            int userId = payload.getUserId();
            String cardIssuanceBank = payload.getCardIssuanceBank();

            CreditCard creditCard = new CreditCard();
            creditCard.setNumber(creditCardNum);
            creditCard.setIssuanceBank(cardIssuanceBank);

            int creditCardId = creditCardService.addCreditCardToUser(userId, creditCard);
            return new ResponseEntity<Integer>(creditCardId, HttpStatusCode.valueOf(200));
        }catch (BusinessException e){
            return new ResponseEntity<Integer>(400, HttpStatusCode.valueOf(400));
        }
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        //       return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return null
        try{
            List<CreditCardView> creditCardViewList = userService.getAllCardOfUser(userId);
            return new ResponseEntity<List<CreditCardView>>(creditCardViewList, HttpStatusCode.valueOf(200));
        }catch (Exception e){
            LOG.error("Error in getting all credit card of user: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<List<CreditCardView>>(new ArrayList<>(), HttpStatusCode.valueOf(400));
        }
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        //       Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        try{
            int userId = creditCardService.getUserIdForCreditCard(creditCardNumber);
            return new ResponseEntity<Integer>(userId, HttpStatusCode.valueOf(200));
        }catch (Exception e){
            LOG.error("Error in getting user id for credit card: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<Integer>(400, HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<List<String>> postTransactionForCreditCard(@RequestBody UpdateBalancePayload[] payload) {
        //      Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.
        List<String> resList = new ArrayList<>();
        try{
            for(UpdateBalancePayload p : payload){
                String creditCardNumber = p.getCreditCardNumber();
                Instant date = p.getTransactionTime();
                double balance = p.getCurrentBalance();

                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setBalance(balance);
                balanceHistory.setDate(date);

                balanceHistory = balanceHistoryService.addBalanceHistoryToCreditCard(creditCardNumber, balanceHistory);

                String res = "Success: " + creditCardNumber + " " + date + " " + balance;
                resList.add(res);
            }
            return new ResponseEntity<List<String>>(resList, HttpStatusCode.valueOf(200));
        }catch (Exception e) {
            LOG.error("Error in posting transaction for credit card: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<List<String>>(List.of("400"), HttpStatusCode.valueOf(400));
        }
    }
    
}
