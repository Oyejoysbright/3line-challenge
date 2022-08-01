package org.jpc.providers.models;

import lombok.Data;

@Data
public class ChangePinRequest {
    private String oldPin, newPin;
}
