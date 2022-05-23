package com.kchonov.springdocker.service;

import com.kchonov.springdocker.cron.ICleanup;
import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.exception.MerchantIsInactiveException;
import com.kchonov.springdocker.exception.NoSuchMerchantException;
import com.kchonov.springdocker.repository.MerchantRepository;
import com.kchonov.springdocker.repository.TransactionRepository;
import com.kchonov.springdocker.utils.Utilities;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService implements ICleanup {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);
    
    private final MerchantRepository merchantRepository;
    
    private final TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
    
    public Transaction findByUUID(String uuid) {
        return transactionRepository.findByUuid(uuid);
    }

    @Modifying
    public Transaction insertOne(Transaction newTransaction) {
        Merchant merchant = merchantRepository.findByEmail(newTransaction.getCustomerEmail());
        if (merchant == null) 
            throw new NoSuchMerchantException("Merchant not found");
        if (!merchant.isActive())
            throw new MerchantIsInactiveException("Merchant is inactive");

        return transactionRepository.save(newTransaction);
    }

    @Transactional
    public void insertAll(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }
    
    public List<Transaction> findbyEmail(String email) {
        return transactionRepository.findByCustomerEmail(email);
    }
    
    public void deleteOne(String id) {
        Transaction transaction = transactionRepository.findByUuid(id);
        if (transaction != null) {
            Merchant merchant = merchantRepository.findByEmail(transaction.getCustomerEmail());
            if (merchant == null) 
                throw new NoSuchMerchantException("Merchant not found");
            if (!merchant.isActive())
                throw new MerchantIsInactiveException("Merchant is inactive");
            
            transactionRepository.delete(transaction);
        }
    }

    @Modifying
    @Transactional
    @Override
    public void clean(long timestamp) {
        transactionRepository.deleteByTimestampCreatedLessThan(Utilities.fixTimezone(timestamp));
    }
}
