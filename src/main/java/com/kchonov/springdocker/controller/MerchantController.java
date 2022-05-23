package com.kchonov.springdocker.controller;

import java.util.List;

import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.messages.ResponseMessage;
import com.kchonov.springdocker.service.MerchantService;
import com.kchonov.springdocker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MERCHANT')")
    @GetMapping("/")
    @ResponseBody
    public List<Merchant> getAll() {
        return merchantService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<ResponseMessage> importMerchants(@CurrentSecurityContext(expression = "authentication?.name") String username, @RequestParam("file") MultipartFile file) {
        logger.info(username + " is trying to import " + file.getOriginalFilename());
        return merchantService.importMerchants(file);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Merchant> updateMerchant(@PathVariable("id") Long id, @RequestBody Merchant merchant) {
        Merchant merchantData = merchantService.findById(id);
        if (merchantData != null) {
            merchantData.setDescription(merchant.getDescription());
            merchantData.setName(merchant.getName());
            merchantData.setStatus(merchant.getStatus());
            return new ResponseEntity<>(merchantService.insertOne(merchantData), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMerchant(@CurrentSecurityContext(expression = "authentication?.name") String username, @PathVariable("id") Long id) {
        Merchant merchant = merchantService.findById(id);
        if (merchant != null) {
            List<Transaction> transactions = transactionService.findbyEmail(merchant.getEmail());
            if (transactions.isEmpty()) {
                merchantService.delete(id);
                logger.info(username + " deleted merchant " + merchant.getEmail());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {

            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
