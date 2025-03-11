package com.store.CamelitesMinimart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.store.CamelitesMinimart.service.ReceiptPrintService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/print")
@RequiredArgsConstructor
@Validated
public class ReceiptPrintController {
    private final ReceiptPrintService receiptPrintService;

    @GetMapping("/test")
    public ResponseEntity<String> getAllCartItems(){
        return ResponseEntity.ok().body(receiptPrintService.printReceipt());
    }


}
