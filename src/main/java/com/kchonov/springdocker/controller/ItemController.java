package com.kchonov.springdocker.controller;

import java.util.List;

import com.kchonov.springdocker.entity.Item;
import com.kchonov.springdocker.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@PreAuthorize("isAuthenticated()")
public class ItemController {

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    // @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_MERCHANT')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    @GetMapping("/items")
    @ResponseBody
    public List<Item> items() {
        logger.info("Listing all items");
        return itemService.findAll();
    }
}
