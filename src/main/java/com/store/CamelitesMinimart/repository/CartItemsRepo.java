package com.store.CamelitesMinimart.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.CamelitesMinimart.entity.CartId;
import com.store.CamelitesMinimart.entity.CartItems;

public interface CartItemsRepo extends JpaRepository<CartItems, CartId>{

    @Query("SELECT i FROM CartItems i where i.cart_id = :cartid")
    List<CartItems> getCartItems(Long cartid);
}
