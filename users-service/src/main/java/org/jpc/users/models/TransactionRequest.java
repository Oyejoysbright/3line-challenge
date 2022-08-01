package org.jpc.users.models;

import org.jpc.jspring.enums.CurrencyEnum;

import lombok.Data;

@Data
public class TransactionRequest {
    private String source;
    private String amount;
    private String cardNumber;
    private String cvv;
    private String expiryMonth;
    private String expiryYear;
	private String pin;
    private CurrencyEnum currency;
    private String email;
    private String fullname;
    private String txtRef;
    private String phoneNumber;
	private String clientIp, deviceFingerprint;
	private Object meta;
	private Boolean preAuthorize;
    private Boolean saveCard;
}
