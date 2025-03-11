package com.store.CamelitesMinimart.service;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.output.PrinterOutputStream;

import javax.print.PrintService;
import java.io.IOException;

public class ReceiptPrintService {
    public String printReceipt(){
        try {
            PrintService printService = PrinterOutputStream.getPrintServiceByName("");

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

}
