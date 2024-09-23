package com.example.SmartContactManager.controller;

import com.example.SmartContactManager.dao.UserRepository;
import com.example.SmartContactManager.entities.Contact;
import com.example.SmartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        String username = principal.getName();
        System.out.println("Username: "+username);

        User user = userRepository.getUserByUserName(username);
        System.out.println("USER: "+ user);

        model.addAttribute("user", user);
    }


    @RequestMapping("/index")
    public String dashBoard(Model model, Principal principal){
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    //add contact handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    //processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, Principal principal){
        String name = principal.getName();
        User user = userRepository.getUserByUserName(name);
        contact.setUser(user);
        user.getContacts().add(contact);
        userRepository.save(user);

        System.out.println("DATA: "+ contact);
        System.out.println("Added to database.");
        return "normal/add_contact_form";
    }
}
