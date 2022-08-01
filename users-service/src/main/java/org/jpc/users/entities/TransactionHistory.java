package org.jpc.users.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jpc.tool.enums.TransactionMethod;
import org.jpc.tool.enums.TransactionType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ref", "transactionId"})
    }
)
public class TransactionHistory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false)
    private String ref, customerId;
    private String transactionId;

    @CreationTimestamp
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private TransactionMethod method;

    private double amount, superWalletPercent, clientWalletPercent, creatorWalletPercent;

    private boolean approved = false;
    private boolean completed = false;
    private boolean superWalletReceived = false;
    private boolean providerWalletReceived = false;

    public TransactionHistory(String customerId, String ref, String transactionId, TransactionType type, TransactionMethod method,
            double amount, boolean approved) {
        this.customerId = customerId;        
        this.ref = ref;
        this.transactionId = transactionId;
        this.type = type;
        this.method = method;
        this.amount = amount;
        this.approved = approved;
    }

    public TransactionHistory(String customerId, String ref, String transactionId, TransactionType type, TransactionMethod method,
            double amount, boolean approved, boolean completed) {
        this.customerId = customerId;        
        this.ref = ref;
        this.transactionId = transactionId;
        this.type = type;
        this.method = method;
        this.amount = amount;
        this.approved = approved;
        this.completed = completed;
    }
}
