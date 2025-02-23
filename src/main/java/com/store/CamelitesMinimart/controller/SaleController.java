package com.store.CamelitesMinimart.controller;



import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.store.CamelitesMinimart.service.SalesReportService;

import com.store.CamelitesMinimart.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
@Validated
public class SaleController {

    private final SaleService saleService;
    private final SalesReportService salesReportService;

    @PostMapping("/completeSale/{cartid}/{cash}")
    public ResponseEntity<Double> completeSale(@PathVariable Long cartid, @PathVariable Double cash, @RequestHeader Long userId){
        return ResponseEntity.ok().body(saleService.completeSale(cartid, cash, userId));
    }

    @GetMapping("/generate/dayly/report/{userId}")
    public ResponseEntity<byte[]> getSalesReport(@PathVariable Long userId) {
        
        ZoneId zoneId = ZoneId.of("America/New_York");
        LocalDate today = LocalDate.now(zoneId);
        //LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
        String todayDate = today.format(formatter).toUpperCase();

        // Get start and end timestamp for today's date with the required timezone and format
        //ZoneId zoneId = ZoneId.of("America/New_York"); // Adjust the timezone as needed (EST or EDT)
        ZonedDateTime startZonedDateTime = today.atStartOfDay(zoneId); // 00:00:00 with time zone
        ZonedDateTime endZonedDateTime = today.atTime(23, 59, 59, 999999999).atZone(zoneId); // 23:59:59 with time zone

        // Format the ZonedDateTime to match the required format "2025-02-22 17:10:50.380914-04"
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX");
        String formattedStartDate = startZonedDateTime.format(timestampFormatter);
        String formattedEndDate = endZonedDateTime.format(timestampFormatter);

        // Generate the PDF using the service
        byte[] pdfBytes = salesReportService.getSalesReport(formattedStartDate, formattedEndDate, userId);

        // Customize the filename
        String fileName = "DailySalesReportFor"+userId +"On"+todayDate+".pdf";

        // Set the content disposition and the content type
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

}
