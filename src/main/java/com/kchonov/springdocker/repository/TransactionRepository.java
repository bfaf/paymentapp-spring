package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.AbstractTransaction;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Krasi
 */
public interface TransactionRepository<T extends AbstractTransaction> extends JpaRepository<T, Long> {

    @Modifying
    @Transactional
    void deleteByTimestampCreatedLessThan(Date offset);
    
    AbstractTransaction findByUuid(String uuid);
    
    List<AbstractTransaction> findByReferenceId(String referenceId);
    
    List<AbstractTransaction> findByCustomerEmail(String customerEmail);
    
    List<AbstractTransaction> findByReferenceIdAndStatus(String referenceId, String status);
}
