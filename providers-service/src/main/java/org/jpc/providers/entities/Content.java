/*
 * This file is generate by Joysbright for 3line
 */
package org.jpc.providers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@Entity
@Getter
@Setter
public class Content {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(nullable = false)
    private String creatorId;
    @Column(nullable = false)
    private double amount;
    @Lob
    private String contentData;
}
