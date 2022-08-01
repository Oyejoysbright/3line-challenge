package org.jpc.providers.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

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
    private String ref;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String transactionId;
    @Column(nullable = false)
    private String narration = "Commission";

    @CreationTimestamp
    private LocalDateTime creationDateTime;

    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private TransactionMethod method;

    private double amount;
}
