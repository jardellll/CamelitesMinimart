package com.store.CamelitesMinimart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.store.CamelitesMinimart.entity.CartItems;
import com.store.CamelitesMinimart.service.CartItemsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/cartItems")
@RequiredArgsConstructor
@Validated
public class CartItemsController {
    private final CartItemsService cartItemsService;

    @GetMapping("/")
    public ResponseEntity<List<CartItems>> getAllCartItems(){
        return ResponseEntity.ok().body(cartItemsService.getAllCartItems());
    }

    @PostMapping("/addItem")
    public ResponseEntity<CartItems> saveEmployee(@RequestBody CartItems cartItems){
        return ResponseEntity.ok().body(cartItemsService.saveCartItems(cartItems));
    }

    @PostMapping("/listCartItems/{cartid}")
    public ResponseEntity<List<CartItems>> listCartItems(@PathVariable Long cartid){
        return ResponseEntity.ok().body(cartItemsService.getCartItems(cartid));
    }

    //checkout signal to add up all the items in the cart, returns 
    @PostMapping("/checkout/{cartid}")
    public ResponseEntity<Double> checkout(@PathVariable Long cartid){
        return ResponseEntity.ok().body(cartItemsService.checkout(cartid));
    }


    //completeSale, passing in the amount received from the customer so we can then we can return the correct amount of change
}
