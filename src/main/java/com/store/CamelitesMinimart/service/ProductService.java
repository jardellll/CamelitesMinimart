package com.store.CamelitesMinimart.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.store.CamelitesMinimart.NewProductRequest;
import com.store.CamelitesMinimart.entity.Product;
import com.store.CamelitesMinimart.repository.ProductRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepo productRepo;
    private StockService stockService;

    @Lazy
    @Autowired
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Product getProductById(Long id){
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (optionalProduct.isPresent()){
            return optionalProduct.get();
        }
        return null;
    }

    public Product saveProduct (Product product){
        // product.setId(product.getId());
        // product.setName(product.getName());
        // product.setPrice(product.getPrice());
        // product.setDetails(product.getDetails());

        Product savedProduct = productRepo.save(product);
        return savedProduct;
    }

    public Product saveNewProduct (NewProductRequest newProductRequest){
        Product product = new Product();
        
        product.setName(newProductRequest.getName());
        product.setPrice(newProductRequest.getPrice());
        product.setDetails(newProductRequest.getDetails());
        product.setBarcode(newProductRequest.getBarcode());

        Product savedProduct = productRepo.save(product);

        stockService.addStock(savedProduct.getId(), newProductRequest.getQuantity());

        return savedProduct;
    }

    public Product updateProduct(Product product){
        // Optional<Product> existingProduct = productRepo.findById(product.getId());
        
        Product updateProduct = productRepo.save(product);

        return updateProduct;
    }

    public void deleteProductById(Long id){
        productRepo.deleteById(id);
    }
}
