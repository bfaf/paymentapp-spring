package com.kchonov.springdocker.entity;

import com.kchonov.springdocker.entity.validators.Validators;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    private String uuid;
    @NotNull
    @Range(min = 1)
    private Long amount;
    @NotNull
    @Pattern(regexp = Validators.TRANSACTION_STATUS)
    private String status;
    @NotNull
    @Email
    private String customerEmail;
    @NotNull
    @Pattern(regexp = Validators.PHONE_NUMBER)
    private String customerPhone;
    @NotNull
    private String referenceId;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampCreated;
}
