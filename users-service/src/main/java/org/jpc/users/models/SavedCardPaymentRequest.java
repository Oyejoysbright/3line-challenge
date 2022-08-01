package org.jpc.users.models;

import lombok.Data;

@Data
public class SavedCardPaymentRequest<T> {
    private String amount;
    private String token;
	private T otherData;
}
