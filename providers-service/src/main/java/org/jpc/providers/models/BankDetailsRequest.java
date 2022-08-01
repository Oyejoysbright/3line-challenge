package org.jpc.providers.models;

import lombok.Data;


@Data
public class BankDetailsRequest {
    private String bankCode, bankName, accountNumber;
}
