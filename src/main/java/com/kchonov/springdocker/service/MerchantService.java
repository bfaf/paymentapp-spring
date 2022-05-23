package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.repository.MerchantRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository merchantRepository;
    
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }
    
    public void insertOne(Merchant newMerchant) {
        merchantRepository.save(newMerchant);
    }
    
    @Transactional
    public void insertAll(List<Merchant> transactions) {
        merchantRepository.saveAll(transactions);
    }
}
