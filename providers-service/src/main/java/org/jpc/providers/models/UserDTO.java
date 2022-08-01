package org.jpc.providers.models;


import org.jpc.providers.entities.Institution;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jpc.providers.entities.ContentCreator;

@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class UserDTO {
    private String customerId;
    private String fullName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String accessToken;
    private String refreshToken;
    private Double balance = 0.0;
    private Double sungaSaveBalance = 0.0;
    private Double vaultSaveBalance = 0.0;
    private Double goalSaveBalance = 0.0;
    private Double groupSaveBalance = 0.0;
    private Double investmentBalance = 0.0;

    public UserDTO(Institution ins) {
        this.customerId = ins.getId();
        this.firstName = ins.getFirstName();
        this.lastName = ins.getLastName();
        this.fullName = firstName + " " + lastName;
        this.phoneNumber = ins.getPhoneNumber();
        this.accessToken = ins.getAccessToken();
        this.refreshToken = ins.getRefreshToken();
        this.balance = ins.getWallet().getBalance();
    }

    public UserDTO(ContentCreator cc) {
        this.customerId = cc.getId();
        this.firstName = cc.getFirstName();
        this.lastName = cc.getLastName();
        this.fullName = firstName + " " + lastName;
        this.phoneNumber = cc.getPhoneNumber();
        this.accessToken = cc.getAccessToken();
        this.refreshToken = cc.getRefreshToken();
        this.balance = cc.getWallet().getBalance();
    }
}
