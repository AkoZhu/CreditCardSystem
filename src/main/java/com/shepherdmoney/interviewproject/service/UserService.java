package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.Exception.BusinessExceptionCode;
import com.shepherdmoney.interviewproject.Exception.BusinessException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import jakarta.annotation.Resource;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    UserRepository userRepository;


    public User addUser(User user) throws BusinessException {
        if(userRepository.getUserById(user.getId()) != null){
            throw new BusinessException(BusinessExceptionCode.USER_ALREADY_EXIST);
        }
        return userRepository.save(user);
    }

    public User createUser(String name, String email) throws BusinessException {
        try{
            User user = new User();
            user.setName(name);
            user.setEmail(email);

            // Possible Error when the user has the same name and email.
            // Maybe some attack, so I use Log to record the error.
            // But we don't throw error because the PK is ID.
            User testUser = userRepository.getUserByNameAndEmail(name, email);
            if(testUser != null){
                LOG.info("Potential Error: Try to create the user with same name and email in DB.");
                LOG.info("User id: " + testUser.getId() +  "; name: " + name + "; email: " + email);
            }

            user = addUser(user);
            return user;
        }catch (Exception e){
            LOG.error("Error in createUser: " +  e.getMessage());
            throw new BusinessException(BusinessExceptionCode.CREATE_USER_FAILED);
        }
    }

    public User getUserById(int userId){
        return userRepository.getUserById(userId);
    }

    public User getUserByNameAndEmail(String name, String email){
        return userRepository.getUserByNameAndEmail(name, email);
    }

    public void deleteUserById(int userId) throws BusinessException {
        if(userRepository.getUserById(userId) == null){
            throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }

    public void deleteUser(int userId) throws BusinessException {
        try{
            deleteUserById(userId);
        }catch (Exception e) {
            LOG.error("Error in deleteUser: " + e.getMessage());
            throw new BusinessException(BusinessExceptionCode.DELETE_USER_FAILED);
        }
    }

    public List<CreditCard> getCreditCardsByUserId(int userId) throws BusinessException {
        try{
            User user = getUserById(userId);
            if(user == null){
                throw new BusinessException(BusinessExceptionCode.USER_NOT_FOUND);
            }
            return user.getCreditCardList();
        }catch (Exception e){
            LOG.error("Error in getCreditCardsByUserId: " + e.getMessage());
            throw new BusinessException(BusinessExceptionCode.GET_CREDIT_CARDS_FAILED);
        }
    }

    public List<CreditCardView> creditCardToView(List<CreditCard> creditCardList) throws BusinessException {
        try {
            List<CreditCardView> creditCardViewList = new ArrayList<>();
            for(CreditCard creditCard : creditCardList){
                CreditCardView creditCardView = CreditCardView.builder()
                        .issuanceBank(creditCard.getIssuanceBank())
                        .number(creditCard.getNumber()).build();
                creditCardViewList.add(creditCardView);
            }
            return creditCardViewList;
        }catch (Exception e){
            LOG.error("Error in creditCardToView: " + e.getMessage());
            throw new BusinessException(BusinessExceptionCode.GET_CREDIT_CARDS_FAILED);
        }
    }

    public List<CreditCardView> getAllCardOfUser(int userId) throws BusinessException {
        try{
            List<CreditCard> creditCardList = getCreditCardsByUserId(userId);
            return creditCardToView(creditCardList);
        }catch (Exception e){
            LOG.error("Error in getAllCardOfUser: " + e.getMessage());
            throw new BusinessException(BusinessExceptionCode.GET_CREDIT_CARDS_FAILED);
        }
    }

//    public User addCreditCardToUser(int userId, CreditCard creditCard){
//        User user = getUserById(userId);
//        if(user == null){
//            throw new BussinessException(BusinessExceptionCode.USER_NOT_FOUND);
//        }
//        user.addCreditCard(creditCard);
//        return userRepository.save(user);
//    }

//    public User removeCreditCardFromUser(int userId, CreditCard creditCard){
//        User user = getUserById(userId);
//        if(user == null){
//            throw new BussinessException(BusinessExceptionCode.USER_NOT_FOUND);
//        }
//        user.removeCreditCard(creditCard);
//        return userRepository.save(user);
//    }


}
