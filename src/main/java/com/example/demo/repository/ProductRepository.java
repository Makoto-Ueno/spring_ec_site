package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	Page<Product> findByStatus(int status, Pageable pageable);

	Page<Product> findAll(Pageable pageable);
}
