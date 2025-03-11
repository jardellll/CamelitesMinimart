package com.store.CamelitesMinimart.service;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.output.PrinterOutputStream;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ReceiptPrintService {
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
