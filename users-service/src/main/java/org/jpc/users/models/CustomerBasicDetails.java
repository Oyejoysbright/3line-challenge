package org.jpc.users.models;

import java.util.UUID;

import org.jpc.jspring.enums.CurrencyEnum;
import org.jpc.users.configs.JwtTokenUtil;
import org.jpc.users.entities.Customer;
import org.jpc.users.repos.RepoCustomer;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class CustomerBasicDetails {
    private final JwtTokenUtil jwtTokenUtil;
    private final RepoCustomer repoCustomer;
    private CurrencyEnum currency = CurrencyEnum.ZMW;
    private String firstName, lastName, fullName, emailAddress, txRef, phoneNumber;

    public CustomerBasicDetails get() {
        Customer customer = jwtTokenUtil.getCustomer();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.fullName = firstName + " " + lastName;
        this.emailAddress = customer.getEmailAddress();
        this.phoneNumber = customer.getPhoneNumber();
        this.txRef = (customer.getId().toString())+UUID.randomUUID();
        return this;
    }

    public CustomerBasicDetails get(String username) {
        Customer customer = repoCustomer.findByEmailAddressOrPhoneNumber(username, username).get();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.fullName = firstName + " " + lastName;
        this.emailAddress = customer.getEmailAddress();
        this.phoneNumber = customer.getPhoneNumber();
        this.txRef = (customer.getId().toString())+UUID.randomUUID();
        return this;
    }
}
