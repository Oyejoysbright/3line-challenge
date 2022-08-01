package org.jpc.providers.models;

import lombok.Data;

@Data
public class CreatePinRequest {
    private String username, pin;
}
