package org.jpc.users.models;

import lombok.Data;

@Data
public class CreatePinRequest {
    private String username, pin;
}
