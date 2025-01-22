package com.store.CamelitesMinimart.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.store.CamelitesMinimart.entity.Stock;
import com.store.CamelitesMinimart.service.StockService;

@Controller
@RequestMapping("/stock")
@RequiredArgsConstructor
@Validated
public class StockController {
    private final StockService stockService;

    @GetMapping("/")
    public String userLogin(Model model){
         model.addAttribute("stock", stockService.getAllStock());
        return "stock";
    }

    @PostMapping("/saveStock")
    public ResponseEntity<Stock> saveStock(@RequestBody Stock stock){
        return ResponseEntity.ok().body(stockService.saveStock(stock));
    }
    @PostMapping("/addStock/{productId}/{quantity}")
    public ResponseEntity<Stock> addStock(@PathVariable Long productId,@PathVariable Integer quantity){
        return ResponseEntity.ok().body(stockService.addStock(productId, quantity));
    }
    @PostMapping("/subtractStock/{productId}/{quantity}")
    public ResponseEntity<Stock> subtractStock(@PathVariable Long productId,@PathVariable Integer quantity){
        return ResponseEntity.ok().body(stockService.subtractStock(productId, quantity));
    }
    @PostMapping("/checkStock/{productId}")
    public ResponseEntity<Integer> checkStock(@PathVariable Long productId){
        return ResponseEntity.ok().body(stockService.stockCheck(productId));
    }


}
