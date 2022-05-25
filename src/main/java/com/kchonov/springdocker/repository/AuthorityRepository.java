package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Krasi
 */
public interface AuthorityRepository extends JpaRepository<Authorities, String> {
    
}
