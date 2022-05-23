package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.repository.MerchantRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Krasi
 */
public class MerchantServiceTest {
    
    private MerchantRepository merchantRepository = Mockito.mock(MerchantRepository.class);
    
    private MerchantService merchantService;
    
    private List<Merchant> merchants;
    
    public MerchantServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        merchantService = new MerchantService(merchantRepository);
        
        merchants = new ArrayList<>();
        merchants.add(new Merchant("John", "", "test1@test.com", "active", 100l, new Date(1653111490)));
        merchants.add(new Merchant("Alex", "", "test2@test.com", "active", 100l, new Date(1653111490)));
        merchants.add(new Merchant("Sandra", "", "test3@test.com", "active", 100l, new Date(1653111490)));
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of findAll method, of class MerchantService.
     */
    @Test
    public void testFindAll() {
        when(merchantRepository.findAll()).thenReturn(merchants);
        
        List<Merchant> result = merchantService.findAll();
        assertEquals(merchants, result);

    }

    /**
     * Test of insertOne method, of class MerchantService.
     */
    @Test
    public void testInsertOne() {
        Merchant newMerchant = merchants.get(0);
        
        when(merchantRepository.save(any(Merchant.class))).thenReturn(null);
        
        merchantService.insertOne(newMerchant);
        verify(merchantRepository, times(1)).save(Mockito.eq(newMerchant));
    }

    /**
     * Test of insertAll method, of class MerchantService.
     */
    @Test
    public void testInsertAll() {
        when(merchantRepository.save(any(Merchant.class))).thenReturn(null);
        
        merchantService.insertAll(merchants);
        verify(merchantRepository, times(1)).saveAll(Mockito.eq(merchants));
    }
    
}