package com.store.CamelitesMinimart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.store.CamelitesMinimart.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
@Validated
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/completeSale/{cartid}/{cash}")
    public ResponseEntity<Double> completeSale(@PathVariable Long cartid, @PathVariable Double cash, @RequestHeader Long userId){
        return ResponseEntity.ok().body(saleService.completeSale(cartid, cash, userId));
    }

}
