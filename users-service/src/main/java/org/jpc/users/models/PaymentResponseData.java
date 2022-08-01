package org.jpc.users.models;

import org.jpc.jspring.flutterwave.dtos.CardResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PaymentResponseData {
    private Object transactionId, txId, paymentId;
    private Object txRef, orderRef, flwRef, raveRef;
    private Object amount, chargedAmount, appFee;
    private Object redirectUrl, deviceFingerprint, ip;
    private Object currency, status;
    @JsonIgnore
    private CardResponse card = new CardResponse();
}
