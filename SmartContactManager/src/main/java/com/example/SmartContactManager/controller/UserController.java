package com.example.SmartContactManager.controller;

import com.example.SmartContactManager.dao.UserRepository;
import com.example.SmartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/index")
    public String dashBoard(Model model, Principal principal){

        String username = principal.getName();
        System.out.println("Username: "+username);

        User user = userRepository.getUserByUserName(username);
        System.out.println("USER: "+ user);

        model.addAttribute("user", user);
//        return "about";
        return "normal/user_dashboard";
    }
}
