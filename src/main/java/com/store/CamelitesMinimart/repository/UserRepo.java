package com.store.CamelitesMinimart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.store.CamelitesMinimart.entity.user;

public interface UserRepo extends JpaRepository<user, Long>{

}
