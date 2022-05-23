package com.kchonov.springdocker.entity;

import com.kchonov.springdocker.entity.validators.Validators;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
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
    
    @NotNull
    private String name;
    private String description;
    
    @Id
    @NotNull
    @Email
    private String email;
    
    @NotNull
    @Pattern(regexp = Validators.MERCHANT_STATUS)
    private String status;
    
    @NotNull
    private Long totalTransactionSum;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampCreated;
    
}