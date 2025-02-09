package com.store.CamelitesMinimart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.store.CamelitesMinimart.AddItemWBarcodeRequest;
import com.store.CamelitesMinimart.entity.CartId;
import com.store.CamelitesMinimart.entity.CartItems;
import com.store.CamelitesMinimart.repository.CartItemsRepo;
import com.store.CamelitesMinimart.entity.Product;
//import com.store.CamelitesMinimart.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemsService {
    private final CartItemsRepo cartItemsRepo;
    private final ProductService productService;

    public List<CartItems> getAllCartItems(){
        return cartItemsRepo.findAll();
    }


    public CartItems saveCartItems (CartItems cartItem){

        CartItems savedCartItems = cartItemsRepo.save(cartItem);//cartItem.getCart_id(),cartItem.getProduct_id());

        return savedCartItems;
    }
    public CartItems saveCartItemsBarcode (AddItemWBarcodeRequest barcodeItem){


        Product p = productService.getProductByBarcode(barcodeItem.getBarcode());

        if (p != null){
            Long id = p.getId();

            CartItems cartItem = new CartItems(barcodeItem.getCart_id(), id, barcodeItem.getQuantity());
            CartItems savedCartItems = cartItemsRepo.save(cartItem);

            return savedCartItems;
        }
        //cartItem.getCart_id(),cartItem.getProduct_id());
        return null;
        
    }


    public CartItems removeCartItems (CartItems cartItem){

        // Optional <CartItems> existingCartItem = cartItemsRepo.findById(cartItem);
        // if (existingCartItem.isPresent()){
        //     cartItemsRepo.delete(existingCartItem.get());

        //     return existingCartItem.get();
        // }
        cartItemsRepo.delete(cartItem);

        //cartItemsRepo.removeFromCart(cartItem.getCart_id(), cartItem.getProduct_id());
        return null;
        
    }

    public List<CartItems> getCartItems(Long cartid){
        return cartItemsRepo.getCartItems(cartid);
    }

    public Double checkout(Long cartid){

        List<CartItems> items = cartItemsRepo.getCartItems(cartid);
        Double total = 0.0;
        for (int i=0; i< items.size(); i++){
            Long productId = items.get(i).getProduct_id();

            Product p = productService.getProductById(productId);

            total += items.get(i).getQuantity() * p.getPrice();
        }

        return total;
    }

    
    
}
