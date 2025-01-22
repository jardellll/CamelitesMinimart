package com.store.CamelitesMinimart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.store.CamelitesMinimart.LoginRequest;
import com.store.CamelitesMinimart.entity.user;
import com.store.CamelitesMinimart.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

import java.util.List;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    
    // @GetMapping("/")
    // public ResponseEntity<List<user>> getAllUsers(){
    //     return ResponseEntity.ok().body(userService.getAllUsers());
    // }

    @GetMapping("/")
    public String userLogin(Model model){
         model.addAttribute("users", userService.getAllUsers() );
        return "login";
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<user> getUserById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<user> addNewUser(@RequestBody user user){
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean>loginUser(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        return ResponseEntity.ok().body(userService.auth(username, password));
    }
    
}
