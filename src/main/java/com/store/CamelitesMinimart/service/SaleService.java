package com.store.CamelitesMinimart.service;

import org.springframework.stereotype.Service;

import com.store.CamelitesMinimart.SaleResponse;
import com.store.CamelitesMinimart.entity.CartItems;
import com.store.CamelitesMinimart.entity.Product;
import com.store.CamelitesMinimart.entity.Sale;
import com.store.CamelitesMinimart.repository.SaleRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {
    private final SaleRepo saleRepo;
    private final CartItemsService cartItemsService;
    private final StockService stockService;

    public List<Sale> getAllSales(){
        return saleRepo.findAll();
    }

    public Sale getSaleById(Long id){
        Optional<Sale> optionalSale = saleRepo.findById(id);
        if (optionalSale.isPresent()){
            return optionalSale.get();
        }
        return null;
    }

    public Sale getSaleByTranId(String tranId){
        Optional<Sale> optionalSale = saleRepo.findByTranId(tranId);
        if (optionalSale.isPresent()){
            return optionalSale.get();
        }
        return null;
    }

    public Sale saveSale (Sale sale){
        Sale savedSale = saleRepo.save(sale);
        return savedSale;
    }

    public void deleteSaleById(Long id){
        saleRepo.deleteById(id);
    }

    public SaleResponse completeSale(Long CartId, Double cash, Long userId, String tranId){
        SaleResponse saleResponse = new SaleResponse();
        Double total = cartItemsService.checkout(CartId);

        if (cash == 0.0){
            cash = total;
        }
        double change = cash - total;

        saleResponse.setChange(change);
        Sale sale = new Sale();

        sale.setCart_id(CartId);
        sale.setUser_id(userId);
        sale.setTotal(total);
        sale.setCash(cash);
        sale.setChange(change);
        sale.setTranId(tranId);
        List<CartItems> items = cartItemsService.getCartItems(CartId);
        for (int i=0; i< items.size(); i++){
            Long productId = items.get(i).getProduct_id();
            Integer quantity = items.get(i).getQuantity();

            stockService.subtractStock(productId, quantity);
        }



        saleRepo.save(sale);

        int retries = 5;
        int waitMs = 500;
        boolean confirmed = false;

        for (int i = 0; i < retries; i++) {
            if (getSaleByTranId(tranId) != null) {
                confirmed = true;
                saleResponse.setVerified(true);
                break;
            }
            try {
                Thread.sleep(waitMs); // wait and try again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (!confirmed) {
            saleResponse.setVerified(false);
            System.err.println("Transaction failed to confirm in DB: tranId = " + tranId + " total was: "+total+" change was "+change+" CartId was "+ CartId);

        }

        return saleResponse;
    }
}
