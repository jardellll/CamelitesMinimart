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
@Table(name = "product")
public class Product {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String details;
    private String barcode;

    // public Product(){}

    // public Product(Long id, String name, double price, String details, String barcode){
    //     this.id = id;
    //     this.name = name;
    //     this.price = price;
    //     this.details = details;
    //     this.barcode = barcode;
    // }

    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getName() {
    //     return name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // public double getPrice() {
    //     return price;
    // }
    // public void setPrice(double price) {
    //     this.price = price;
    // }

    // public String getDetails() {
    //     return details;
    // }

    // public void setDetails(String details) {
    //     this.details = details;
    // }

    // public String getBarcode() {
    //     return barcode;
    // }

    // public void setBarcode(String barcode) {
    //     this.barcode = barcode;
    // }

}
