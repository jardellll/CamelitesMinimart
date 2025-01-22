package com.store.CamelitesMinimart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.store.CamelitesMinimart.StockResponse;
import com.store.CamelitesMinimart.entity.Product;
import com.store.CamelitesMinimart.entity.Stock;
import com.store.CamelitesMinimart.repository.StockRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StockRepo stockRepo;
    private final ProductService productService;

    public List<StockResponse> getAllStock(){
        List<Stock> stock = stockRepo.findAll();
        List<StockResponse> response = new ArrayList<>();
        for (int i=0; i< stock.size(); i++){
            Product product = productService.getProductById(stock.get(i).getProductId());
            StockResponse item = new StockResponse();
            item.setProductId(stock.get(i).getProductId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setDetails(product.getDetails());
            item.setQuantity(stock.get(i).getQuantity());

            response.add(item);

        }
        return response;

    }

    public Stock getStockById(Long id){
        Optional<Stock> optionalStock = stockRepo.findById(id);
        if(optionalStock.isPresent()){
            return optionalStock.get();
        }
        return null;
    }

    public Stock saveStock(Stock stock){
        Stock savedStock = stockRepo.save(stock);
        return savedStock;
    }

    public Stock addStock (Long id, Integer addition){
        Stock stock = getStockById(id);
        if (stock != null){
            stock.setQuantity(stock.getQuantity()+addition);
            stockRepo.save(stock);
            return stock;
        }else{
            Stock newStock = new Stock();
            newStock.setProductId(id);
            newStock.setQuantity(addition);
            saveStock(newStock);
            // System.err.println("this item does not exist");
            return newStock;
        }
        
    }
    public Stock subtractStock (Long id, Integer deduction){
        Stock stock = getStockById(id);
        if (stock != null){
            stock.setQuantity(stock.getQuantity()-deduction);
            stockRepo.save(stock);
            return stock;
        }else{
            System.err.println("this item does not exist");
            return null;
        }
        
    }
    public Integer stockCheck (Long id){
        Stock stock = getStockById(id);
        if (stock != null){
            return stock.getQuantity();
        }else{
            System.err.println("this item does not exist");
            return null;
        }
        
    }


}
