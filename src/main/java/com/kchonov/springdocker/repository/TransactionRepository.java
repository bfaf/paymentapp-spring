package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.Transaction;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Krasi
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Modifying
    @Transactional
    void deleteByTimestampCreatedLessThan(Date offset);
}
