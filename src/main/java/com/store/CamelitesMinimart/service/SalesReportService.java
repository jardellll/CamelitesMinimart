package com.store.CamelitesMinimart.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.transaction.Transactional;

import org.springframework.jdbc.core.RowMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font;
import com.itextpdf.text.Element;

@Service
public class SalesReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public byte[] getSalesReport(String startDate, String endDate, Long userId){
        String sql = "SELECT * FROM sales_by_user WHERE sale_completed_ts BETWEEN CAST(? AS timestamp with time zone) AND CAST(? AS timestamp with time zone) AND user_id = ?";
        System.out.println("start date: " + startDate);
        System.out.println("end date: " + endDate);
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> viewData= jdbcTemplate.queryForList(sql, startDate, endDate, userId);
        long endTime = System.currentTimeMillis();
        System.out.println("Query Execution Time: " + (endTime - startTime) + "ms");    

        System.out.println("Records Retrieved: " + viewData.size());
        for (Map<String, Object> row : viewData) {
            System.out.println(row);
        }


        double totalSum = viewData.stream()
                              .mapToDouble(row -> Double.parseDouble(row.get("total").toString()))
                              .sum();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            System.out.println("Records Retrieved: " + viewData.size());
            generateDaySalesReportPdf(viewData, totalSum, byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    
    }
    // do I want create a view that already sums up sales by the day or use the query to group it that way?? Should I lump in all id's or separate by id?
    //think I have separating it by id be something that is just added to the query if needed but
    public byte[] getSummaryReport(String startDate, String endDate, Long userId){
        String sql = "SELECT * FROM total_sales_by_day WHERE day BETWEEN CAST(? AS timestamp with time zone) AND CAST(? AS timestamp with time zone)";
        System.out.println("start date: " + startDate);
        System.out.println("end date: " + endDate);
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> viewData= jdbcTemplate.queryForList(sql, startDate, endDate);
        long endTime = System.currentTimeMillis();
        System.out.println("Query Execution Time: " + (endTime - startTime) + "ms");    

        System.out.println("Records Retrieved: " + viewData.size());
        for (Map<String, Object> row : viewData) {
            System.out.println(row);
        }


        double totalSum = viewData.stream()
                              .mapToDouble(row -> Double.parseDouble(row.get("total_sales").toString()))
                              .sum();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            System.out.println("Records Retrieved: " + viewData.size());
            generateSummaryReportPdf(viewData, totalSum, byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    
    }

    private void generateDaySalesReportPdf(List<Map<String, Object>> viewData, double totalSum, ByteArrayOutputStream outputStream) throws IOException {
        // Use iText to generate the PDF based on the data
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph header = new Paragraph("Camelite's Minimart Daily Sales Report", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);  // Center the header
        try {
            document.add(header);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Add some spacing between the header and the table
        try {
            document.add(new Paragraph(" "));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Add the table and content to the PDF
        PdfPTable table = new PdfPTable(5); // Example, adjust based on your columns

        table.addCell("User ID");
        table.addCell("Sale Completed Timestamp");
        table.addCell("Total");
        table.addCell("Cash");
        table.addCell("Change");
            


        for (Map<String, Object> row : viewData) {
                table.addCell(row.get("user_id").toString());
                table.addCell(row.get("sale_completed_ts").toString());
                table.addCell(row.get("total").toString());
                table.addCell(row.get("cash").toString());
                table.addCell(row.get("change").toString());
                
        }
        table.addCell(""); 
        table.addCell(""); 
        table.addCell(""); 
        table.addCell("Total sales for the day:"); 
        table.addCell(String.format("%.2f", totalSum));
            
            

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }

    private void generateSummaryReportPdf(List<Map<String, Object>> viewData, double totalSum, ByteArrayOutputStream outputStream) throws IOException {
        // Use iText to generate the PDF based on the data
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph header = new Paragraph("Camelite's Minimart Total Sales Report", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);  // Center the header
        try {
            document.add(header);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Add some spacing between the header and the table
        try {
            document.add(new Paragraph(" "));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Add the table and content to the PDF
        PdfPTable table = new PdfPTable(2); // Example, adjust based on your columns

        table.addCell("Date");
        table.addCell("Total Sales");
            


        for (Map<String, Object> row : viewData) {
                table.addCell(row.get("day").toString().substring(0, 10));
                table.addCell(row.get("total_sales").toString());
                
        }
        table.addCell("Total sales for the period:"); 
        table.addCell(String.format("%.2f", totalSum));
            
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }


}
