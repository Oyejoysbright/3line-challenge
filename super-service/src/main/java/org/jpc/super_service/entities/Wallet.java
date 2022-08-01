/*
 * This file is generate by Joysbright for 3lines
 */
package org.jpc.super_service.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jpc.tool.utils.Helper;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@Entity
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private double balance = 0.0;

    @CreationTimestamp
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public void setBalance(double bal) {
        this.balance = Helper.parseMoney(bal);
    }
}
