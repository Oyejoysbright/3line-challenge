package org.jpc.users.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@SuppressWarnings("unchecked")
@Configuration
public class Properties {

    @Autowired
    Environment environment;

    public <T> T get(String key) {
        return (T) environment.getProperty(key);
    }

    public String getCompanyName() {
        return environment.getProperty("company_name");
    }

    public String getJwtSecret() {
        return environment.getProperty("jwt-secret");
    }

    public long getJwtExpirationInMs() {
        return Long.valueOf(environment.getProperty("jwt_expiration_in_min")) * 60000;
    }

    public long getJwtOtherTokenInMs() {
        return Long.valueOf(environment.getProperty("jwt_other_token_in_min")) * 60000;
    }

    public long getRefreshExpirationInMs() {
        return Long.valueOf(environment.getProperty("jwt_refresh_token_expiration_in_hr")) * 3600000;
    }

    public int getOtpExpiry() {
        return Integer.valueOf(environment.getProperty("otp_expiry_in_min"));
    }

    public String getRefundableAmount() {
        return environment.getProperty("refundable_amount");
    }

    public int getOtpLength() {
        return Integer.valueOf(environment.getProperty("otp_length"));
    }

    public String[] getFlutterwaveConfig() {
        String[] res = {
            get("flw-sec-key"),
            get("flw-pub-key"),
            get("flw-enc-key"),};
        return res;
    }

    public double getSuperWalletPercent() {
        return Double.valueOf(environment.getProperty("super_wallet_percent"));
    }

    public double getClientWalletPercent() {
        return Double.valueOf(environment.getProperty("client_wallet_percent"));
    }

    public double getCreatorWalletPercent() {
        return Long.valueOf(environment.getProperty("creator_wallet_percent"));
    }
    
    public String getSuperWalletUrl() {
        return environment.getProperty("super_wallet_url");
    }
    
    public String getProvidersWalletUrl() {
        return environment.getProperty("providers_url");
    }
    
    public String getSuperWalletSecret() {
        return environment.getProperty("super_wallet_secret");        
    }
    
    public String getProvidersWalletSecret() {
        return environment.getProperty("providers_wallet_secret");        
    }

}
