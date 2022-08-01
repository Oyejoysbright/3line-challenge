package org.jpc.tool.utils;

import java.text.DecimalFormat;

import org.jpc.tool.models.CustomResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class Helper {
    public static final CustomResponse RESPONSE = new CustomResponse();
    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");
    public static final RestConnector REST_CONNECTOR = new RestConnector();

    public static String maskCard(String last4) {
        return "**** **** **** "+last4;
    }

    public static Double parseMoney(double raw) {
        return Double.parseDouble(MONEY_FORMAT.format(raw));
    }
    
    public static String anyToString(Object arg) {
        try {
            return MAPPER.writeValueAsString(arg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> T stringToAny(String arg, Class<T> returnClass) {
        try {
            return MAPPER.readValue(arg, returnClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static HttpHeaders updateBearerToken(HttpHeaders headers, String secKey) {
        headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + secKey);
        return headers;
    }
    
    public static String getBearerValue(String arg){
        if (arg == null) {
            throw new IllegalArgumentException("Authorization value cannot be null");
        }
        if (!arg.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization value must begin with Bearer ");
        }
        return arg.substring(7);
    }
    
    public static Double getPercentageValue(double percent, double overall, boolean isMoney) {
        double val = (percent / 100) * overall;
        if (isMoney) {
            return parseMoney(val);
        }
        return val;
    }
}
