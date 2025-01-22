package com.store.CamelitesMinimart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.store.CamelitesMinimart.entity.Stock;

@Repository
public interface StockRepo extends JpaRepository<Stock, Long>{

}
