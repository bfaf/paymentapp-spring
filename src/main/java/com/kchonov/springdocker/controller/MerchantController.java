package com.kchonov.springdocker.controller;

import java.util.List;

import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.messages.ResponseMessage;
import com.kchonov.springdocker.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1")
public class MerchantController {

    Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;

    // @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_MERCHANT')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/items")
    @ResponseBody
    public List<Merchant> items() {
        logger.info("Listing all items");
        return merchantService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/import/merchants")
    public ResponseEntity<ResponseMessage> importMerchants(@RequestParam("file") MultipartFile file) {
        return this.merchantService.importMerchants(file);
    }

}