package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.Merchant;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Krasi
 */
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

    @Modifying
    @Transactional
    void deleteByTimestampCreatedLessThan(Date offset);
    
    Merchant findByEmail(String email);
}
