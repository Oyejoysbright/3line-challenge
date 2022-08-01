package org.jpc.users.models;

import lombok.Data;

@Data
public class VerifyOtpRequest<T> {
    private String flwRef, otp;
	private T otherData;
}
