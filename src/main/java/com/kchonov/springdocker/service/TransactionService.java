package com.kchonov.springdocker.service;

import com.kchonov.springdocker.cron.ICleanup;
import com.kchonov.springdocker.entity.AbstractTransaction;
import com.kchonov.springdocker.entity.AuthorizeTransaction;
import com.kchonov.springdocker.entity.ChargeTransaction;
import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.RefundedTransaction;
import com.kchonov.springdocker.entity.ReversalTransaction;
import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.entity.constants.Constants;
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

    private final TransactionRepository<AbstractTransaction> transactionRepository;

    private final TransactionRepository<ChargeTransaction> chargeTransactionRepository;

    public List<AbstractTransaction> findAll() {
        return transactionRepository.findAll();
    }

    public AbstractTransaction findByUUID(String uuid) {
        return transactionRepository.findByUuid(uuid);
    }

    @Modifying
    public AbstractTransaction insertOne(Transaction newTransaction) {
        Merchant merchant = merchantRepository.findByEmail(newTransaction.getCustomerEmail());
        if (merchant == null) {
            throw new NoSuchMerchantException("Merchant not found");
        }
        if (!merchant.isActive()) {
            throw new MerchantIsInactiveException("Merchant is inactive");
        }

        AbstractTransaction n = (AbstractTransaction) newTransaction;
        AbstractTransaction t = setTransactionType(n, merchant);
        return transactionRepository.save(t);
    }

    /*
    I don't like this method. There must be a better way to implement this logic
     */
    public AbstractTransaction setTransactionType(AbstractTransaction newTransaction, Merchant merchant) {
        List<AbstractTransaction> referencedTransactions = transactionRepository.findByReferenceIdAndStatus(newTransaction.getReferenceId(), Constants.TransactionStatusType.APPROVED.getValue());
        chargeTransactionRepository.findByReferenceId(newTransaction.getReferenceId());
        if (referencedTransactions.isEmpty() && newTransaction.getAmount() > 0 && newTransaction.isApproved()) {
            AuthorizeTransaction newTransacton = new AuthorizeTransaction(
                    newTransaction.getUuid(),
                    newTransaction.getAmount(),
                    newTransaction.getStatus(),
                    newTransaction.getCustomerEmail(),
                    newTransaction.getCustomerPhone(),
                    newTransaction.getReferenceId(),
                    null,
                    null
            );
            return newTransacton;
        } else if (referencedTransactions.size() > 0
                && newTransaction.getAmount() > 0
                && newTransaction.isApproved()) {
            merchant.acceptPayment(newTransaction.getAmount());
            merchantRepository.save(merchant);
            ChargeTransaction newTransacton = new ChargeTransaction(
                    newTransaction.getUuid(),
                    newTransaction.getAmount(),
                    newTransaction.getStatus(),
                    newTransaction.getCustomerEmail(),
                    newTransaction.getCustomerPhone(),
                    newTransaction.getReferenceId(),
                    null,
                    null
            );
            return newTransacton;
        } else if (referencedTransactions.size() > 1 && hasChargeTransaction(newTransaction) && newTransaction.isRefunded()) {
            RefundedTransaction at = new RefundedTransaction(
                    newTransaction.getUuid(),
                    newTransaction.getAmount(),
                    newTransaction.getStatus(),
                    newTransaction.getCustomerEmail(),
                    newTransaction.getCustomerPhone(),
                    newTransaction.getReferenceId(),
                    null,
                    null
            );
            at.setStatus(Constants.TransactionStatusType.REFUNDED.getValue());
            merchant.refundPayment(newTransaction.getAmount());
            merchantRepository.save(merchant);
            return at;
        } else if (referencedTransactions.size() == 1
                && referencedTransactions.get(0).isApproved()
                && newTransaction.getAmount() == 0
                && newTransaction.isReversed()) {
            ReversalTransaction at = new ReversalTransaction(
                    newTransaction.getUuid(),
                    newTransaction.getAmount(),
                    newTransaction.getStatus(),
                    newTransaction.getCustomerEmail(),
                    newTransaction.getCustomerPhone(),
                    newTransaction.getReferenceId(),
                    null,
                    null
            );
            at.setStatus(Constants.TransactionStatusType.REVERSED.getValue());
            return at;
        }

        newTransaction.setStatus(Constants.TransactionStatusType.ERROR.getValue());
        return newTransaction;
    }

    public boolean hasChargeTransaction(AbstractTransaction transaction) {
        return chargeTransactionRepository.findByReferenceId(transaction.getReferenceId()) != null;
    }

    @Transactional
    public void insertAll(List<AbstractTransaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    public List<AbstractTransaction> findbyEmail(String email) {
        return transactionRepository.findByCustomerEmail(email);
    }

    public void deleteOne(String id) {
        AbstractTransaction transaction = transactionRepository.findByUuid(id);
        if (transaction != null) {
            Merchant merchant = merchantRepository.findByEmail(transaction.getCustomerEmail());
            if (merchant == null) {
                throw new NoSuchMerchantException("Merchant not found");
            }
            if (!merchant.isActive()) {
                throw new MerchantIsInactiveException("Merchant is inactive");
            }

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
