package org.jpc.users.models;

import lombok.Data;

@Data
public class ChangePasswordReq {
    private String oldPassword, newPassword;
}
