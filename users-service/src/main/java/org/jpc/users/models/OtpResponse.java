package org.jpc.users.models;

import java.util.HashMap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class OtpResponse {
    private String medium, reference, otp, expiry;

    public OtpResponse(Object rawData) {
        HashMap<String, String> rawObject = (HashMap) rawData;
        this.expiry = rawObject.get("expiry");
        this.medium = rawObject.get("medium");
        this.otp = rawObject.get("otp");
        this.reference = rawObject.get("reference");
    }
}
