package org.jpc.providers.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jpc.tool.utils.Helper;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private double balance = 0.0;
    private double unsettledBalance = 0.0;

    @CreationTimestamp
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<TransactionHistory> histories = new ArrayList<>();
    
    public void setBalance(double bal) {
        this.balance = Helper.parseMoney(bal);
    }

}
