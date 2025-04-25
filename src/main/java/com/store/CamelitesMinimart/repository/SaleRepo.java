package com.store.CamelitesMinimart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.store.CamelitesMinimart.entity.Sale;

public interface SaleRepo extends JpaRepository<Sale, Long>{
    Optional<Sale> findByTranId(String tranId);
}
