package com.store.CamelitesMinimart.controller;

import org.springframework.http.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
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
    @GetMapping("/receipt/{cartId}")
    public ResponseEntity<byte[]> printReceipt(@PathVariable Long cartId) {
    String receiptImagePath = receiptPrintService.getReceipt(cartId);

        try {
            File file = new File("receipt.png");
            byte[] imageBytes = Files.readAllBytes(file.toPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
