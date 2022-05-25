package com.kchonov.springdocker.entity;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Krasi
 */
@Entity
@DiscriminatorValue("reversal")
public class ReversalTransaction extends AbstractTransaction {
    public ReversalTransaction() {}
    
    public ReversalTransaction(String uuid, Long amount, String status, String customerEmail, String customerPhone, String referenceId, String merchantEmail, Date timestampCreated) {
        super(uuid, amount, status, customerEmail, customerPhone, referenceId, merchantEmail, timestampCreated);
    }
}
