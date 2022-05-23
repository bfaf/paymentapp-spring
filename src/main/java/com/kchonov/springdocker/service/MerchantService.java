package com.kchonov.springdocker.service;

import com.kchonov.springdocker.cron.ICleanup;
import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.repository.MerchantRepository;
import com.kchonov.springdocker.utils.Utilities;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MerchantService implements ICleanup {
    
    Logger logger = LoggerFactory.getLogger(MerchantService.class);
    
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

    @Override
    @Modifying
    @Transactional
    public void clean(long timestamp) {
        merchantRepository.deleteByTimestampCreatedLessThan(Utilities.fixTimezone(timestamp));
    }
}
