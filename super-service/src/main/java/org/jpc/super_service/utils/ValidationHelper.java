package org.jpc.super_service.utils;

public class ValidationHelper {
    public static String getDescriptiveMessage(String msg) {
        if (msg.equalsIgnoreCase("must be null")) {
            return "Field not needed. Consider removing it";
        } else {
            return msg;            
        }
    }
}
