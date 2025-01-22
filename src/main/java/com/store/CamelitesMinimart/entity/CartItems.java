package com.store.CamelitesMinimart.entity;

import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@IdClass(CartId.class)
@Table(name = "cartitems")
public class CartItems {
    @Id
    private Long cart_id;
    @Id
    private Long product_id;

    private Integer quantity;

    // public Long getProduct_id(){
    //     return product_id;
    // }

    // public void setProduct_id(Long product_id){
    //     this.product_id = product_id;
    // }
}
