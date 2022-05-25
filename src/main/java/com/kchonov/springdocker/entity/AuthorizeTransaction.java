package com.kchonov.springdocker.entity;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author Krasi
 */
@Entity
@DiscriminatorValue("authorize")
public class AuthorizeTransaction extends AbstractTransaction {
    
    public AuthorizeTransaction() {}
    
    public AuthorizeTransaction(String uuid, Long amount, String status, String customerEmail, String customerPhone, String referenceId, String merchantEmail, Date timestampCreated) {
        super(uuid, amount, status, customerEmail, customerPhone, referenceId, merchantEmail, timestampCreated);
    }
}
