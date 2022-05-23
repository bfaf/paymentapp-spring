package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Krasi
 */
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {
    
}
