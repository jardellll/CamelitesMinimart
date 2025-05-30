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
import com.store.CamelitesMinimart.SaleResponse;
import com.store.CamelitesMinimart.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
@Validated
public class SaleController {

    private final SaleService saleService;
    private final SalesReportService salesReportService;

    @PostMapping("/completeSale/{cartid}/{cash}/{tranId}")
    public ResponseEntity<SaleResponse> completeSale(@PathVariable Long cartid, @PathVariable Double cash, @RequestHeader Long userId, @PathVariable String tranId){
        return ResponseEntity.ok().body(saleService.completeSale(cartid, cash, userId, tranId));
    }

    @GetMapping("/generate/dayly/report/{userId}/{reportDate}")
    public ResponseEntity<byte[]> getSalesReport(@PathVariable Long userId, @PathVariable String reportDate) {
        
        LocalDate date = LocalDate.parse(reportDate, DateTimeFormatter.ISO_LOCAL_DATE);
        ZoneId zoneId = ZoneId.of("America/New_York");
        //LocalDate today = LocalDate.now(zoneId);
        //LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
        String repDate = date.format(formatter).toUpperCase();

        // Get start and end timestamp for today's date with the required timezone and format
        //ZoneId zoneId = ZoneId.of("America/New_York"); // Adjust the timezone as needed (EST or EDT)
        ZonedDateTime startZonedDateTime = date.atStartOfDay(zoneId); // 00:00:00 with time zone
        ZonedDateTime endZonedDateTime = date.atTime(23, 59, 59, 999999999).atZone(zoneId); // 23:59:59 with time zone

        // Format the ZonedDateTime to match the required format "2025-02-22 17:10:50.380914-04"
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX");
        String formattedStartDate = startZonedDateTime.format(timestampFormatter);
        String formattedEndDate = endZonedDateTime.format(timestampFormatter);

        // Generate the PDF using the service
        byte[] pdfBytes = salesReportService.getSalesReport(formattedStartDate, formattedEndDate, userId);

        // Customize the filename
        String fileName = "DailySalesReportFor"+userId +"On"+repDate+".pdf";

        // Set the content disposition and the content type
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/generate/summary/report/{userId}/{startDate}/{endDate}")
    public ResponseEntity<byte[]> getSalesReport(@PathVariable Long userId, @PathVariable String startDate,  @PathVariable String endDate) {
        
        LocalDate startDateFormatted = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDateFormatted = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        ZoneId zoneId = ZoneId.of("America/New_York");
        //LocalDate today = LocalDate.now(zoneId);
        //LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
        String repStartDate = startDateFormatted.format(formatter).toUpperCase();
        String repEndDate = endDateFormatted.format(formatter).toUpperCase();

        // Get start and end timestamp for today's date with the required timezone and format
        //ZoneId zoneId = ZoneId.of("America/New_York"); // Adjust the timezone as needed (EST or EDT)
        ZonedDateTime startZonedDateTime = startDateFormatted.atStartOfDay(zoneId); // 00:00:00 with time zone
        ZonedDateTime endZonedDateTime = endDateFormatted.atTime(23, 59, 59, 999999999).atZone(zoneId); // 23:59:59 with time zone

        // Format the ZonedDateTime to match the required format "2025-02-22 17:10:50.380914-04"
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX");
        String formattedStartDate = startZonedDateTime.format(timestampFormatter);
        String formattedEndDate = endZonedDateTime.format(timestampFormatter);

        // Generate the PDF using the service
        byte[] pdfBytes = salesReportService.getSummaryReport(formattedStartDate, formattedEndDate, userId);

        // Customize the filename
        String fileName = "SummarySalesReportBetween"+repStartDate+"and"+repEndDate+".pdf";

        // Set the content disposition and the content type
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
