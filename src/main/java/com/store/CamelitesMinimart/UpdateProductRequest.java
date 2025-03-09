package com.store.CamelitesMinimart;

public class UpdateProductRequest {

    private String name;
    private double price;
    private String details;
    private String barcode;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
