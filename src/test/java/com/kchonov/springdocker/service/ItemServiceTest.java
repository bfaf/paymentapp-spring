package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.Item;
import com.kchonov.springdocker.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author Krasi
 */
public class ItemServiceTest {
    
    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);;
    
    private ItemService itemService;
    
    public ItemServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        itemService = new ItemService(itemRepository);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of findAll method, of class ItemService.
     */
    @Test
    public void testFindAll() {
        System.out.println("findAll");
        List<Item> expResult = new ArrayList<>();
        expResult.add(new Item(1, "John"));
        expResult.add(new Item(2, "Alex"));
        expResult.add(new Item(3, "Sandra"));
        
        when(itemRepository.findAll()).thenReturn(expResult);
        
        List<Item> result = itemService.findAll();
        assertEquals(expResult, result);
    }
    
}
