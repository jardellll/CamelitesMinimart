package com.store.CamelitesMinimart.service;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.output.PrinterOutputStream;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import jakarta.transaction.Transactional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.store.CamelitesMinimart.entity.Sale;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font;
import com.itextpdf.text.Element;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReceiptPrintService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public String getReceipt(Long cart_id){
        //Long cart_id =  sale.getCart_id();
        String sql = "SELECT cartitems.*, products.name, products.price FROM public.cartitems as cartitems JOIN public.product as products ON cartitems.product_id = products.id WHERE cartitems.cart_id = ?";
        List<Map<String, Object>> viewData= jdbcTemplate.queryForList(sql, cart_id );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            generatePdf(viewData, byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage receiptImage = convertPdfToImage(byteArrayOutputStream.toByteArray());
        //return byteArrayOutputStream.toByteArray();
        return saveImageAndGetURL(receiptImage);
    }

    private void generatePdf(List<Map<String, Object>> viewData, ByteArrayOutputStream outputStream) throws IOException{
        int baseHeight = 200; // Header and footer space
        int itemHeight = 20; // Height per item
        int totalHeight = baseHeight + (itemHeight * viewData.size());
        
        Rectangle pageSize = new Rectangle(226, totalHeight);
        Document document = new Document(pageSize, 5, 5, 0, 0);
        try {
            PdfWriter.getInstance(document, outputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();

        Font font = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

        // Header
        try {
            //document.add(new Paragraph("Camelite's Wholesale\n", boldFont));
            Paragraph header = new Paragraph("Camelite's Wholesale\n", boldFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = LocalDateTime.now().format(formatter);
            document.add(new Paragraph("Date: " + formattedDate + "\n", font));
            document.add(new Paragraph("--------------------------------\n", font));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        double grandTotal = 0.0;

         // Add some spacing between the header and the table
         for (Map<String, Object> item : viewData) {
            String productName = (String) item.get("name");
            double price = (double) item.get("price");
            int quantity = (int) item.get("quantity");
            double total = price * quantity;
    
            grandTotal += total;
    
            String lineItem = String.format("%-15s %5s x %.2f\n", productName, quantity, price);
            String lineTotal = String.format("Item Total: %.2f\n", total);
    
            try {
                document.add(new Paragraph(lineItem, font));
                document.add(new Paragraph(lineTotal, font));
                document.add(new Paragraph("--------------------------------\n", font));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    
        // Grand Total
        try {
            document.add(new Paragraph("Grand Total: $" + String.format("%.2f", grandTotal) + "\n", boldFont));
            document.add(new Paragraph("--------------------------------\n", font));
            document.add(new Paragraph("Thank you for shopping!\n", font));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();

        
    }

    private BufferedImage convertPdfToImage(byte[] pdfBytes) {
    try {
        PDDocument document = PDDocument.load(pdfBytes);
        PDFRenderer renderer = new PDFRenderer(document);

        // Get total number of pages in case of multiple pages
        int pageCount = document.getNumberOfPages();
        List<BufferedImage> images = new ArrayList<>();

        for (int i = 0; i < pageCount; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, 300); // High DPI for better quality
            images.add(image);
        }
        document.close();

        // If multiple pages, merge images into one long image
        if (images.size() > 1) {
            return mergeImages(images);
        } else {
            return images.get(0);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

// Merging images vertically
private BufferedImage mergeImages(List<BufferedImage> images) {
    int width = images.get(0).getWidth();
    int totalHeight = images.stream().mapToInt(BufferedImage::getHeight).sum();

    BufferedImage combined = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = combined.createGraphics();
    
    int y = 0;
    for (BufferedImage img : images) {
        g2d.drawImage(img, 0, y, null);
        y += img.getHeight();
    }
    g2d.dispose();

    return combined;
}
    private String saveImageAndGetURL(BufferedImage image) {
    try {
        File outputFile = new File("receipt.png");
        ImageIO.write(image, "png", outputFile);
        return "/receipts/receipt.png"; // Return this URL to the frontend
    } catch (IOException e) {
        e.printStackTrace();
    }
        return null;
    }












    public String printReceipt(){
        try {
            //PrintService printService = PrinterOutputStream.getPrintServiceByName("RONGTA 80mm Series Printer");
            String printerName = "RONGTA 80mm Series Printer"; // Replace with actual name
            PrintService printService = findPrintService(printerName);

            if (printService != null) {
                EscPos escpos = new EscPos(new PrinterOutputStream(printService));
                escpos.writeLF("Camelite's Wholesale");
                escpos.close();
                return "printed";
            } else {
                System.out.println("Printer not found!");
                return "no printer found";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    public static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printer : printServices) {
            System.out.println(printer.getName());
            if (printer.getName().equalsIgnoreCase(printerName)) {
                return printer;
            }
        }
        return null;
    }

}
