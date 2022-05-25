package com.kchonov.springdocker.entity;

import com.kchonov.springdocker.entity.constants.Validators;
import com.kchonov.springdocker.entity.validators.ValidEmail;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Merchant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String name;
    
    private String description;
    
    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @Pattern(regexp = Validators.MERCHANT_STATUS)
    private String status;

    @NotNull
    private Long totalTransactionSum;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampCreated;

    public boolean isActive() {
        return status.compareToIgnoreCase("active") == 0;
    }
    
    public void acceptPayment(Long amount) {
        this.totalTransactionSum += amount;
    }
    
    public void refundPayment(Long amount) {
        this.totalTransactionSum -= amount;
    }
}
