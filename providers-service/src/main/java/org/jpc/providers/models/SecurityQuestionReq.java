package org.jpc.providers.models;

import lombok.Data;

@Data
public class SecurityQuestionReq {
    private String question, answer, password;
}
