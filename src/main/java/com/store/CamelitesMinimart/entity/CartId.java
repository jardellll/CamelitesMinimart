package com.store.CamelitesMinimart.entity;

import java.io.Serializable;
import java.util.Objects;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CartId implements Serializable {
    private Long cart_id;
    private Long product_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartId cartId1 = (CartId) o;
        return Objects.equals(cart_id, cartId1.cart_id) && Objects.equals(product_id, cartId1.product_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart_id, product_id);
    }

    public Long getProduct_id(){
        return product_id;
    }

    public void setProduct_id(Long productId){
        this.product_id = productId;
    }
}
