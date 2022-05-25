package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.AbstractTransaction;
import com.kchonov.springdocker.entity.AuthorizeTransaction;
import com.kchonov.springdocker.entity.ChargeTransaction;
import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.RefundedTransaction;
import com.kchonov.springdocker.entity.ReversalTransaction;
import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.entity.constants.Constants;
import com.kchonov.springdocker.repository.ChargeTransactionRepository;
import com.kchonov.springdocker.repository.MerchantRepository;
import com.kchonov.springdocker.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Krasi
 */
public class TransactionServiceTest {
    
    private TransactionRepository<AbstractTransaction> transactionRepository = Mockito.mock(TransactionRepository.class);
    
    private MerchantRepository merchantRepository = Mockito.mock(MerchantRepository.class);
    
    private TransactionRepository<ChargeTransaction> chargeTransactionRepository = Mockito.mock(ChargeTransactionRepository.class);

    private MerchantService merchantService;
    
    private TransactionService transactionService;
    
    public TransactionServiceTest() {
    }
    
    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService(merchantRepository, transactionRepository, chargeTransactionRepository);
        merchantService = new MerchantService(merchantRepository);
    }

    /**
     * Test of setTransactionType method, of class TransactionService.
     */
    @Test
    public void testSetTransactionTypeRefundedSingleCharge() {
        List<AbstractTransaction> testTransactions = new ArrayList<>(List.of(
            new AuthorizeTransaction(null, 50L, Constants.TransactionStatusType.APPROVED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null),
            new ChargeTransaction(null, 5L, Constants.TransactionStatusType.APPROVED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null),
            new RefundedTransaction(null, 5L, Constants.TransactionStatusType.REFUNDED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null)
        ));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>());
        
        AbstractTransaction newTransaction = transactionService.setTransactionType(testTransactions.get(0), null);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.APPROVED.getValue());
        
        Merchant merchant = Mockito.mock(Merchant.class);
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>(List.of(testTransactions.get(0))));
        newTransaction = transactionService.setTransactionType(testTransactions.get(1), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.APPROVED.getValue());
        verify(merchant, times(1)).acceptPayment(Mockito.eq(newTransaction.getAmount()));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>(List.of(testTransactions.get(0), testTransactions.get(1))));
        newTransaction = transactionService.setTransactionType(testTransactions.get(2), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.REFUNDED.getValue());
        verify(merchant, times(1)).refundPayment(Mockito.eq(newTransaction.getAmount()));
    }
    
    @Test
    public void testSetTransactionTypeRefundedMultipleCharges() {
        List<AbstractTransaction> testTransactions = new ArrayList<>(List.of(
            new AuthorizeTransaction(null, 50L, Constants.TransactionStatusType.APPROVED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null),
            new ChargeTransaction(null, 5L, Constants.TransactionStatusType.APPROVED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null),
            new ChargeTransaction(null, 2L, Constants.TransactionStatusType.APPROVED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null),
            new RefundedTransaction(null, 5L, Constants.TransactionStatusType.REFUNDED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null)
        ));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>());
        
        AbstractTransaction newTransaction = transactionService.setTransactionType(testTransactions.get(0), null);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.APPROVED.getValue());
        
        Merchant merchant = Mockito.mock(Merchant.class);
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>(List.of(testTransactions.get(0))));
        newTransaction = transactionService.setTransactionType(testTransactions.get(1), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.APPROVED.getValue());
        verify(merchant, times(1)).acceptPayment(Mockito.eq(newTransaction.getAmount()));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>(List.of(testTransactions.get(0), testTransactions.get(1))));
        newTransaction = transactionService.setTransactionType(testTransactions.get(1), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.APPROVED.getValue());
        verify(merchant, times(2)).acceptPayment(Mockito.eq(newTransaction.getAmount()));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>(List.of(testTransactions.get(0), testTransactions.get(1), testTransactions.get(2))));
        newTransaction = transactionService.setTransactionType(testTransactions.get(3), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.REFUNDED.getValue());
        verify(merchant, times(1)).refundPayment(Mockito.eq(newTransaction.getAmount()));
    }
    
    @Test
    public void testSetTransactionTypeReversal() {
        List<AbstractTransaction> testTransactions = new ArrayList<>(List.of(
            new AuthorizeTransaction(null, 50L, Constants.TransactionStatusType.APPROVED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null),
            new ReversalTransaction(null, 0L, Constants.TransactionStatusType.REVERSED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null)
        ));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>(List.of(testTransactions.get(0))));
        
        Merchant merchant = Mockito.mock(Merchant.class);
        AbstractTransaction newTransaction = transactionService.setTransactionType(testTransactions.get(1), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.REVERSED.getValue());
    }
    
    @Test
    public void testSetTransactionTypeErrorAfterAttemptToRefundAfterAuthorize() {
        List<AbstractTransaction> testTransactions = new ArrayList<>(List.of(
            new AuthorizeTransaction(null, 50L, Constants.TransactionStatusType.APPROVED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null),
            new RefundedTransaction(null, 5L, Constants.TransactionStatusType.REFUNDED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null)
        ));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>(List.of(testTransactions.get(0))));
        
        Merchant merchant = Mockito.mock(Merchant.class);
        AbstractTransaction newTransaction = transactionService.setTransactionType(testTransactions.get(1), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.ERROR.getValue());
    }
    
    @Test
    public void testSetTransactionTypeErrorAfterAttemptToReferseNoTransactions() {
        List<AbstractTransaction> testTransactions = new ArrayList<>(List.of(
            new AuthorizeTransaction(null, 5L, Constants.TransactionStatusType.REVERSED.getValue(), "krasi1@test.com", "123456789", "ref1", "merch1@test.com", null)
        ));
        
        when(transactionRepository.findByReferenceIdAndStatus(anyString(), anyString())).thenReturn(new ArrayList<>());
        
        Merchant merchant = Mockito.mock(Merchant.class);
        AbstractTransaction newTransaction = transactionService.setTransactionType(testTransactions.get(0), merchant);
        assertEquals(newTransaction.getStatus(), Constants.TransactionStatusType.ERROR.getValue());
    }
    
}
