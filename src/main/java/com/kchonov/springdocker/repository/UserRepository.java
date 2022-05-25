package com.kchonov.springdocker.repository;

import com.kchonov.springdocker.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Krasi
 */
public interface UserRepository extends JpaRepository<Users, Long> {
    public String findByUsername(String username);
}
