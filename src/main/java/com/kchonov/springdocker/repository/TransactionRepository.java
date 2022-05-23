package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.Transaction;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Krasi
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Modifying
    @Transactional
    void deleteByTimestampCreatedLessThan(Date offset);
    
    Transaction findByUuid(String uuid);
    
    List<Transaction> findByCustomerEmail(String customerEmail);
}
