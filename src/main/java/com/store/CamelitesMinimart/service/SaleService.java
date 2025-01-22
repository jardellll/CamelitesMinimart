package com.store.CamelitesMinimart.service;

import org.springframework.stereotype.Service;

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

    public Sale saveSale (Sale sale){
        Sale savedSale = saleRepo.save(sale);
        return savedSale;
    }

    public void deleteSaleById(Long id){
        saleRepo.deleteById(id);
    }

    public Double completeSale(Long CartId, Double cash, Long userId){
        Double total = cartItemsService.checkout(CartId);

        double change = cash - total;


        Sale sale = new Sale();

        sale.setCart_id(CartId);
        sale.setUser_id(userId);
        sale.setTotal(total);
        sale.setCash(cash);
        sale.setChange(change);
        List<CartItems> items = cartItemsService.getCartItems(CartId);
        for (int i=0; i< items.size(); i++){
            Long productId = items.get(i).getProduct_id();
            Integer quantity = items.get(i).getQuantity();

            stockService.subtractStock(productId, quantity);
        }



        saleRepo.save(sale);

        return change;
    }
}
