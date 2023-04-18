package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.Exception.BusinessExceptionCode;
import com.shepherdmoney.interviewproject.Exception.BussinessException;
import com.shepherdmoney.interviewproject.model.User;
import jakarta.annotation.Resource;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    UserRepository userRepository;


    public User addUser(User user){
        if(userRepository.getUserById(user.getId()) != null){
            throw new BussinessException(BusinessExceptionCode.USER_ALREADY_EXIST);
        }
        return userRepository.save(user);
    }

    public User createUser(String name, String email){
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
            throw new BussinessException(BusinessExceptionCode.CREATE_USER_FAILED);
        }
    }

    public User getUserById(int userId){
        return userRepository.getUserById(userId);
    }

    public User getUserByNameAndEmail(String name, String email){
        return userRepository.getUserByNameAndEmail(name, email);
    }

    public void deleteUserById(int userId) throws BussinessException{
        if(userRepository.getUserById(userId) == null){
            throw new BussinessException(BusinessExceptionCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }

    public void deleteUser(int userId){
        try{
            deleteUserById(userId);
        }catch (Exception e) {
            LOG.error("Error in deleteUser: " + e.getMessage());
            throw new BussinessException(BusinessExceptionCode.DELETE_USER_FAILED);
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
