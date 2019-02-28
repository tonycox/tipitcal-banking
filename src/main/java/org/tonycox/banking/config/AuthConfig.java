package org.tonycox.banking.config;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public PasswordEncryptor encryptor() {
        return new BasicPasswordEncryptor();
    }
}
