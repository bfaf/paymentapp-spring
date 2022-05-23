package com.kchonov.springdocker.service;

import com.kchonov.springdocker.cron.ICleanup;
import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.repository.TransactionRepository;
import com.kchonov.springdocker.utils.Utilities;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService implements ICleanup {

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

    @Modifying
    @Transactional
    public void clean(long timestamp) {
        transactionRepository.deleteByTimestampCreatedLessThan(Utilities.fixTimezone(timestamp));
    }
}
