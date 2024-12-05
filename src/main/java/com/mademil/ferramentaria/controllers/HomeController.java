package com.mademil.ferramentaria.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mademil.ferramentaria.repositories.UserRepository;
import com.mademil.ferramentaria.entities.User;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/")
    public String serveHomePage(
        @AuthenticationPrincipal UserDetails springUser,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }

        User user = userRepository.findByUsername(springUser.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        
        return "home";
    }
}
