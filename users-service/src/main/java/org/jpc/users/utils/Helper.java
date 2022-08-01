package org.jpc.users.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jpc.jspring.enums.CountryCodeEnum;
import org.jpc.jspring.enums.CurrencyEnum;
import org.jpc.jspring.enums.MediumEnum;
import org.jpc.jspring.flutterwave.FlutterwavePayment;
import org.jpc.jspring.flutterwave.dtos.CustomerData;
import org.jpc.jspring.flutterwave.dtos.MoMoPaymentPayload;
import org.jpc.jspring.flutterwave.dtos.SendOtpPayload;
import org.jpc.users.configs.Properties;
import org.jpc.users.entities.PaymentSource;
import org.jpc.users.enums.PaymentSourceEnum;
import org.jpc.users.models.CustomerBasicDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.jpc.tool.models.CustomResponse;

@Configuration
@RequiredArgsConstructor
public class Helper {
    private final Properties properties;
    private final CustomerBasicDetails customerDetails;
    public static final CustomResponse RESPONSE = new CustomResponse();
    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final MoMoPaymentPayload moMoPaymentPayload = new MoMoPaymentPayload("mobilemoneyzambia", "Zambia");
    public static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");
    private FlutterwavePayment paymentProcessor = null;

    public FlutterwavePayment getPaymentProcessor() {
        if(this.paymentProcessor == null) {
            this.paymentProcessor = FlutterwavePayment.initiate(CountryCodeEnum.ZA, CurrencyEnum.ZMW, properties.getFlutterwaveConfig());
        }
        return paymentProcessor;
    }

    public static List<PaymentSource> PAYMENT_SOURCES() {
        List<PaymentSource> sources = new ArrayList<>();
        PaymentSourceEnum[] enums = PaymentSourceEnum.values();
        for (PaymentSourceEnum each : enums) {
            sources.add(each.source);
        }
        return sources;
    }

    public <T,R> R buildPaymentPayload(T clientReq, R returnPayload) {
        BeanUtils.copyProperties(clientReq, returnPayload);
        BeanUtils.copyProperties(customerDetails.get(), returnPayload);
        return returnPayload;
    }

    public CustomerBasicDetails getCustomerDetails() {
        return customerDetails.get();
    }

    public SendOtpPayload getOtpPayload(String username, MediumEnum... mediums) {
        SendOtpPayload processorPayload = new SendOtpPayload();
        processorPayload.setExpiry(properties.getOtpExpiry());
        processorPayload.setLength(properties.getOtpLength());
        CustomerBasicDetails details = customerDetails.get(username);
        CustomerData data = new CustomerData(
            details.getFullName(),
            details.getEmailAddress(),
            details.getPhoneNumber());
        processorPayload.setCustomer(data);
        processorPayload.setSender(properties.getCompanyName());
        ArrayList<String> midStrings = new ArrayList<>();
        for (int i = 0; i < mediums.length; i++) {
            midStrings.add(mediums[i].text);
        }
        Object[] midStringsObj = midStrings.toArray();
        processorPayload.setMedium(Arrays.copyOf(midStringsObj, midStringsObj.length, String[].class));
        return processorPayload;
    }

    public static String maskCard(String last4) {
        return "**** **** **** "+last4;
    }

    public static Double parseMoney(double raw) {
        return Double.parseDouble(MONEY_FORMAT.format(raw));
    }
}
