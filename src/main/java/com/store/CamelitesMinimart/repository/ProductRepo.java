package com.store.CamelitesMinimart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.store.CamelitesMinimart.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long>{

}
