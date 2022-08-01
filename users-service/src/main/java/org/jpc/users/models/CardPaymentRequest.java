package org.jpc.users.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CardPaymentRequest<T> {
	@NotBlank
	private String cardNumber;
	@Length(max = 3)
	private String cvv;
	private String expiryMonth, expiryYear;
	@Min(value = 1)
	private String amount;
	private String clientIp, deviceFingerprint;
	private Object meta;
	private String pin;
	private Boolean preAuthorize;
	private boolean saveCard = false;
	private T otherData;
}
