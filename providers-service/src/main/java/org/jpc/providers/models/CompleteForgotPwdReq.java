package org.jpc.providers.models;

import lombok.Data;

@Data
public class CompleteForgotPwdReq {
    private String token, newPassword, username;
}
