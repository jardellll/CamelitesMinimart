package com.store.CamelitesMinimart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.store.CamelitesMinimart.entity.Carts;
import com.store.CamelitesMinimart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;

    @PostMapping("/newCart")
    public ResponseEntity<Carts> NewCart(){
        return ResponseEntity.ok().body(cartService.newCart());
    }
}
