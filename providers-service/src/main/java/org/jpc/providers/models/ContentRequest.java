/*
 * This file is generate by Joysbright for 3line
 */
package org.jpc.providers.models;

import javax.persistence.Lob;
import lombok.Data;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@Data
public class ContentRequest {
    private double amount;
    @Lob
    private String contentData;
}
