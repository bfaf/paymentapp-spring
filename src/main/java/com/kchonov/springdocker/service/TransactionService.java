package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.repository.TransactionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
    
    public void insertOne(Transaction newMerchant) {
        transactionRepository.save(newMerchant);
    }
    
    @Transactional
    public void insertAll(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }
}
