package com.store.CamelitesMinimart.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.store.CamelitesMinimart.entity.Carts;
import com.store.CamelitesMinimart.repository.CartRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepo cartRepo;

    public Carts newCart (){

        Carts newCart = new Carts();
        
        cartRepo.save(newCart);

        return newCart;
    }

    public Optional<Carts> deleteCart(Long cartId){
        try{
            return cartRepo.deleteCart(cartId);
        }catch (Exception e){
            log.error("Error while finding user with userId: {} and userType: {}. Error message: {}", cartId, e.getMessage());
        }
        return Optional.empty();
    }



    public Optional<Carts> findCart(Long cartId){
        try{
            return cartRepo.findCart(cartId);
        }catch (Exception e){
            log.error("Error while finding user with userId: {} and userType: {}. Error message: {}",  cartId, e.getMessage());
        }
        return Optional.empty();
    }

    

}
