package org.jpc.users.enums;

import org.jpc.users.entities.PaymentSource;

public enum PaymentSourceEnum {
    NEW_CARD("New Card", "NEW_CARD"),
    SAVING_WALLET("Saving Wallet", "WALLET");

    public final PaymentSource source;

    private PaymentSourceEnum(String name, String value) {
        this.source = new PaymentSource(null, name, value);
    }
}
