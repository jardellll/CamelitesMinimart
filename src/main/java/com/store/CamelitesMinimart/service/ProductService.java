package com.store.CamelitesMinimart.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.store.CamelitesMinimart.NewProductRequest;
import com.store.CamelitesMinimart.UpdateProductRequest;
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

    public Product getProductByBarcode(String barcode){


        // Product exampleProduct = new Product();
        // exampleProduct.setBarcode(barcode);

        // Example <Product> example = Example.of(exampleProduct);

        Optional<Product> optionalProduct = productRepo.findByBarcode(barcode);
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

    public Product updateProduct(UpdateProductRequest updateProduct){
        
        Optional<Product> optionalProduct = productRepo.findById(updateProduct.getId());
        if (optionalProduct.isPresent()){
            Product existingProduct = optionalProduct.get();

            if (updateProduct.getName() != null){
                existingProduct.setName(updateProduct.getName());
            }
            if (updateProduct.getBarcode() != null){
                existingProduct.setBarcode(updateProduct.getBarcode());
            }
            if (updateProduct.getPrice() != 0.0){
                existingProduct.setPrice(updateProduct.getPrice());
            }
            if (updateProduct.getDetails() != null){
                existingProduct.setDetails(updateProduct.getDetails());
            }

            productRepo.save(existingProduct);
            return existingProduct;

        }
        return null;

    }

    public void deleteProductById(Long id){
        productRepo.deleteById(id);
    }
}
