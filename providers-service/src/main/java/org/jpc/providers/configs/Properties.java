package org.jpc.providers.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@SuppressWarnings("unchecked")
@Configuration
public class Properties {
    @Autowired Environment environment;

    public <T> T get(String key) {
        return (T) environment.getProperty(key);
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

    public Object getSecretKey() {
        return environment.getProperty("secret_key");
    }
    
}
