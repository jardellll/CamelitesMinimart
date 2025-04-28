package com.store.CamelitesMinimart.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sale")
public class Sale {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private Long cart_id;
    private double total;
    private double cash;
    private double change;
}
