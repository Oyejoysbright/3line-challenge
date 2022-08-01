package org.jpc.providers.models;

import lombok.Data;

@Data
public class ChangePasswordReq {
    private String oldPassword, newPassword;
}
