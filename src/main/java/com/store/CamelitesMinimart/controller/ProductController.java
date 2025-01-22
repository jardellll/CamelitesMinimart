package com.store.CamelitesMinimart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.store.CamelitesMinimart.NewProductRequest;
import com.store.CamelitesMinimart.entity.Product;
import com.store.CamelitesMinimart.service.ProductService;
import com.store.CamelitesMinimart.service.StockService;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    // @GetMapping("/")
    // public ResponseEntity<List<Product>> getAllProducts(){
    //     return ResponseEntity.ok().body(productService.getAllProducts());
    // }
    @GetMapping("/")
    public String getAllProducts(Model model){
        model.addAttribute("productList", productService.getAllProducts() );
        return "products";
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        return ResponseEntity.ok().body(productService.getProductById(id));
    }
    @PostMapping("/newProduct")
    public ResponseEntity<Product> newProduct(@RequestBody NewProductRequest newProductRequest){
        return ResponseEntity.ok().body(productService.saveNewProduct(newProductRequest));


    }
    // @PostMapping("/")
    // public ResponseEntity<Product> saveEmployee(@RequestBody Product product){
    //     return ResponseEntity.ok().body(productService.saveProduct(product));
    // }
    // @PutMapping("/")
    // public ResponseEntity<Product> updateProduct(@RequestBody Product product){
    //     return ResponseEntity.ok().body(productService.updateProduct(product));
    // }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok().body("deleted product successfully");
    }
}
