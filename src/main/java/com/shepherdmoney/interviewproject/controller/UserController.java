package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.service.UserService;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    // wire in the user repository (~ 1 line)
    @Resource
    UserService userService;

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        //       Create a user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        try{
            String name = payload.getName();
            String email = payload.getEmail();

            User newUser = userService.createUser(name, email);
            return new ResponseEntity<Integer>(newUser.getId(), HttpStatusCode.valueOf(200));
        }catch (Exception e){
            return new ResponseEntity<Integer>(400, HttpStatusCode.valueOf(400));
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        //       Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        try{
            userService.deleteUserById(userId);
            return new ResponseEntity<String>("User deleted", HttpStatusCode.valueOf(200));
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
