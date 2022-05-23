package com.kchonov.springdocker.controller;

import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.service.TransactionService;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MERCHANT')")
    @GetMapping("/")
    @ResponseBody
    public List<Transaction> getAll() {
        return transactionService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) throws URISyntaxException {
        Transaction createdTransaction = transactionService.insertOne(transaction);
        if (createdTransaction == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("uuid") String id, @RequestBody Transaction transaction) throws URISyntaxException {
        Transaction transactionData = transactionService.findByUUID(id);
        if (transactionData != null) {
            transactionData.setAmount(transaction.getAmount());
            transactionData.setCustomerEmail(transaction.getCustomerEmail());
            transactionData.setCustomerPhone(transaction.getCustomerPhone());
            transactionData.setStatus(transaction.getStatus());
            return new ResponseEntity<>(transactionService.insertOne(transactionData), HttpStatus.OK);
        } else {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable("id") String id) {
        transactionService.deleteOne(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
