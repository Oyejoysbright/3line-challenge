package org.jpc.tool.utils;

public class ValidationHelper {
    public static String DESCRIPTIVE_MESSAGE(String msg) {
        if (msg.equalsIgnoreCase("must be null")) {
            return "Field not needed. Consider removing it";
        } else {
            return msg;            
        }
    }
}
