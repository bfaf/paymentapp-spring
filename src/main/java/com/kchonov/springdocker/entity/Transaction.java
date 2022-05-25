package com.kchonov.springdocker.entity;

import com.kchonov.springdocker.entity.constants.Validators;
import com.kchonov.springdocker.entity.validators.UUID;
import com.sun.istack.NotNull;
import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
public class Transaction implements Serializable {

    @Id
    @UUID
    private String uuid;
    
    @NotNull
    @Range(min = 1)
    private Long amount;
    
    @NotNull
    @Pattern(regexp = Validators.TRANSACTION_STATUS)
    protected String status;
    
    @NotNull
    @Email
    private String customerEmail;
    
    @NotNull
    @Pattern(regexp = Validators.PHONE_NUMBER)
    private String customerPhone;
    
    @NotNull
    private String referenceId;
    
    public boolean isApproved() {
        return status.compareToIgnoreCase("approved") == 0;
    }
    
    public boolean isRefunded() {
        return status.compareToIgnoreCase("refunded") == 0;
    }
    
    public boolean isReversed() {
        return status.compareToIgnoreCase("reversed") == 0;
    }
}
