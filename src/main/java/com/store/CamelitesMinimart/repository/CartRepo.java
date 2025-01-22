package com.store.CamelitesMinimart.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.store.CamelitesMinimart.entity.Carts;
import com.store.CamelitesMinimart.entity.CartId;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Carts, Long>{

    @Query("SELECT c FROM Carts c WHERE c.cart_id = :cartId")
    Optional<Carts> findCart(Long cartId);

    @Query("DELETE FROM Carts c WHERE c.cart_id = :cartId")
    Optional<Carts> deleteCart(Long cartId);


}
