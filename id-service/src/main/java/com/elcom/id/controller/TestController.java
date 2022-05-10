package com.elcom.id.controller;

import com.elcom.id.model.User;
import com.elcom.id.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String findUser(@RequestParam("username") String username, @RequestParam("password") String password){
        User user = userService.findByUsernameAndPassword(username, password);
        if(user == null){
            return "Khong tim thay";
        }
        return user.toString();
    }
}
