package com.kchonov.springdocker.entity;

import com.kchonov.springdocker.entity.constants.Constants;
import com.kchonov.springdocker.entity.constants.Validators;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author Krasi
 */
@Entity
@Getter
@Setter
public class AbstractTransaction extends Transaction implements Serializable {

    
    //@NotNull
    //@Pattern(regexp = Validators.TRANSACTION_TYPE)
    //private String type;
    
    @NotNull
    @Email
    private String merchantEmail;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampCreated;
    
    public AbstractTransaction() {}
    
    public AbstractTransaction(String uuid, Long amount, String status, String customerEmail, String customerPhone, String referenceId, String merchantEmail, Date timestampCreated) {
        super(uuid, amount, status, customerEmail, customerPhone, referenceId);
        //this.type = type;
        this.merchantEmail = merchantEmail;
        this.timestampCreated = timestampCreated;
    }
    
    
}
