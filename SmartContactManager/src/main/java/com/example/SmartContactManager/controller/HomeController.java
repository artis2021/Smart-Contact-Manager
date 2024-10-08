package com.example.SmartContactManager.controller;

import com.example.SmartContactManager.dao.UserRepository;
import com.example.SmartContactManager.entities.User;
import com.example.SmartContactManager.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    @GetMapping("/test")
//    @ResponseBody
//    public String test(){
//        User user = new User();
//        user.setId(330);
//        user.setAbout("Software Engineer");
//        user.setEmail("arti@gmail.com");
//        user.setEnabled(true);
//        user.setName("Arti Sahu");
//        user.setPassword("arti");
//        user.setImageUrl("https://images.pexels.com/photos/3225528/pexels-photo-3225528.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2");
//        userRepository.save(user);
//
//        return "working...";
//    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title", "HomePage");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "AboutPage");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title", "RegisterPage");
        model.addAttribute("user", new User());
        System.out.println("User :"+ model.getAttribute("user"));
        return "signup";

    }

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("user") User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, BindingResult result1, HttpSession session){
        try{
            if(!agreement){
                System.out.println("Please agree all the terms and conditions.");
                throw new Exception("Please agree all the terms and conditions.");
            }

            if(result1.hasErrors()){
                System.out.println("ERROR "+result1.toString());
                model.addAttribute("user", user);
                return "signup";
            }


            user.setRole("USER_ROLE");
            user.setEnabled(true);
//        user.setImageUrl("");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            System.out.println("Encrypted Password "+ user.getPassword());
            System.out.println("Agreement "+agreement);
            System.out.println("USER "+user);
            User user1 = userRepository.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully register", "alert-success"));
            System.out.println("After saving in database "+user1);

        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong "+e.getMessage(), "alert-denger"));

        }
        return "signup";
    }

    //handler for custom login
    @GetMapping("/signin")
    public String loginPage(Model model){
        model.addAttribute("title", "Login Page");
        return "login";
    }


}
