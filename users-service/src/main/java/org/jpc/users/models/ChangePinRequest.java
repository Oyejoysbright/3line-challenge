package org.jpc.users.models;

import lombok.Data;

@Data
public class ChangePinRequest {
    private String oldPin, newPin;
}
