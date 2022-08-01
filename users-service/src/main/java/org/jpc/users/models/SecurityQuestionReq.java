package org.jpc.users.models;

import lombok.Data;

@Data
public class SecurityQuestionReq {
    private String question, answer, password;
}
