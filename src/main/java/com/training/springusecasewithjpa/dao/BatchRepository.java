package com.training.springusecasewithjpa.dao;

import com.training.springusecasewithjpa.entities.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, String> {
    // JpaRepository provides all basic CRUD operations automatically
    // You can add custom query methods here if needed
}