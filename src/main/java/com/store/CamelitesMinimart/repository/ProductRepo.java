package com.store.CamelitesMinimart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import com.store.CamelitesMinimart.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long>, QueryByExampleExecutor<Product> {
    Optional<Product> findByBarcode(String barcode);
}