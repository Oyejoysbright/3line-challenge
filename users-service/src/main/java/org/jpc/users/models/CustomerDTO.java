package org.jpc.users.models;

import java.util.List;

import org.jpc.users.entities.Customer;
import org.jpc.users.entities.PaymentSource;
import org.jpc.users.utils.Helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class CustomerDTO {
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
    private List<PaymentSource> paymentSources = Helper.PAYMENT_SOURCES();

    public CustomerDTO(Customer customer) {
        this.customerId = customer.getId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.fullName = firstName + " " + lastName;
        this.phoneNumber = customer.getPhoneNumber();
        this.accessToken = customer.getAccessToken();
        this.refreshToken = customer.getRefreshToken();
        this.balance = customer.getWallet().getBalance();
        this.paymentSources.addAll(customer.getPaymentSources());
    }
}
