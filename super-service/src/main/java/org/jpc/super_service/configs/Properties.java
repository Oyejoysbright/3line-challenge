package org.jpc.super_service.configs;

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
    public String getAllowedOrigin() {
        return environment.getProperty("allowed_origin");
    }
    public String getSecretKey() {
        return environment.getProperty("secret_key");
    }
    
}
