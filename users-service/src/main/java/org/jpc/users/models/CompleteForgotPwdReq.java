package org.jpc.users.models;

import lombok.Data;

@Data
public class CompleteForgotPwdReq {
    private String token, newPassword, username;
}
