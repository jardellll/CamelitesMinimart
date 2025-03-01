package com.store.CamelitesMinimart.service;

import org.springframework.stereotype.Service;

import com.store.CamelitesMinimart.LoginRequest;
import com.store.CamelitesMinimart.LoginResponse;
import com.store.CamelitesMinimart.entity.user;
import com.store.CamelitesMinimart.repository.UserRepo;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    public List<user> getAllUsers(){
        return userRepo.findAll();
    }

    public user getUserById (Long id){
        Optional<user> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        }
        return null;
    }

    public user saveUser (user user){
        
        user.setPassword(encoder.encode(user.getPassword()));
        user savedUser = userRepo.save(user);
        return savedUser;
    }

    public void deleteUserById(Long id){
        userRepo.deleteById(id);
    }

    public LoginResponse auth (String username, String password){
        LoginResponse lr = new LoginResponse();
        List<user> users = getAllUsers();
        for (user user : users){
            String usernameFromDb = user.getUsername();
            String passFromDb = user.getPassword();
            String usernameFromReq = username;
            String passFromReq = password;

            // String usernameFromReq = loginRequest.getUsername();
            // String passFromReq= loginRequest.getPassword();

            // if (loginRequest.getUsername().equals(user.getUsername()) ){
            //     if (encoder.matches(loginRequest.getPassword(), user.getPassword())){
            //         return true;
            //     }
                
            // }
            if (username.equals(user.getUsername()) ){
                if (encoder.matches(password, user.getPassword())){
                    lr.setAuthenticated(true);
                    lr.setId(user.getId());
                    lr.setAccessLevel(user.getAccessLevel());
                    return lr;
                }
                
            }
        }

        lr.setAuthenticated(false);
        lr.setId(null);
        lr.setAccessLevel(null);
        return lr;
    }
}
