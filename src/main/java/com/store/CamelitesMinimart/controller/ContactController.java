package com.store.CamelitesMinimart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
@Validated
public class ContactController {

     @GetMapping("/")
    public String userLogin(Model model){
         model.addAttribute("contact");
        return "contact";
    }
}
