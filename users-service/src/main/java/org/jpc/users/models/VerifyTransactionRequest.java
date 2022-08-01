package org.jpc.users.models;

import org.jpc.jspring.enums.CurrencyEnum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyTransactionRequest {
    private String txRef;
    private Double amount;
    private CurrencyEnum currency;
}
