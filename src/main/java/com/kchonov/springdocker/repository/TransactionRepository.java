package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Krasi
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    
}
