package org.jpc.providers.models;

import lombok.Data;

@Data
public class ConfirmForgotPwdReq {
    private String ref, otp, username;
}
