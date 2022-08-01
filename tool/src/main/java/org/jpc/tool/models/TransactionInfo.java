/*
 * This file is generate by Joysbright for 3line
 */
package org.jpc.tool.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import org.jpc.tool.enums.TransactionMethod;
import org.jpc.tool.enums.TransactionType;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@Data
@NoArgsConstructor
public class TransactionInfo {
    @NotEmpty
    private String ref, userId, transactionId;
    @NotNull
    private TransactionMethod method;
    @NotNull
    private TransactionType type;
    private double amount, superAmount, institutionAmount, creatorAmount;
    @NotEmpty
    private String contentId;
    @NotEmpty
    private String creatorId;
}
