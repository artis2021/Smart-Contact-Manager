package com.example.SmartContactManager.controller;

import com.example.SmartContactManager.dao.ContactRepository;
import com.example.SmartContactManager.dao.UserRepository;
import com.example.SmartContactManager.entities.Contact;
import com.example.SmartContactManager.entities.User;
import com.example.SmartContactManager.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

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
    public String processContact(@ModelAttribute Contact contact, @RequestParam("image") MultipartFile multipartFile, Principal principal, HttpSession session){
        try{
            String name = principal.getName();
            User user = userRepository.getUserByUserName(name);
            if(multipartFile.isEmpty()){
                System.out.println("File is empty.");
            } else {
                contact.setImage(multipartFile.getOriginalFilename());
                File file = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded in static/img folder.");
            }
            contact.setUser(user);
            user.getContacts().add(contact);
            userRepository.save(user);

            System.out.println("DATA: "+ contact);
            System.out.println("Added to database.");
//            set message
            session.setAttribute("message", new Message("Your contact is added. Add more...", "success"));
        } catch (Exception e){
            System.out.println("ERROR: "+ e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong. please try again.", "danger"));
        }

        return "normal/add_contact_form";
    }

    @GetMapping("/show-contacts")
    public String showContacts(Model model, Principal principal){
        model.addAttribute("title", "Show Contacts");
//        String userName = principal.getName();
//        User user = userRepository.getUserByUserName(userName);
//       List<Contact> contacts = user.getContacts();

        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        List<Contact> contacts = contactRepository.findContactsByUser(user.getId());
        model.addAttribute("contacts", contacts);


        return "normal/show_contacts";
    }


}
