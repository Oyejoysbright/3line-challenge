package org.jpc.users.models;

import lombok.Data;

@Data
public class WalletPaymentRequest<T> {
    private Double amount;
    private String pin;
    private T otherData;
}
