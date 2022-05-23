package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.Item;
import com.kchonov.springdocker.repository.ItemRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    @PostConstruct
    public void init() {
        itemRepository.save(new Item(1, "John"));
        itemRepository.save(new Item(2, "Alex"));
        itemRepository.save(new Item(3, "Sandra"));
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
