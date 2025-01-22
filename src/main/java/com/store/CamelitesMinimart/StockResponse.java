package com.store.CamelitesMinimart;


public class StockResponse {
    private Long productId;
    private Integer quantity;
    private String name;
    private double price;
    private String details;
    private String barcode;

    // Getters
    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public String getBarcode() {
        return barcode;
    }

    // Setters
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
