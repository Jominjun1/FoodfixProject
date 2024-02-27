package com.project.foodfix.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@Configuration
@PropertySource("classpath:application.yml")
public class JwtConfig {

    @Value("${spring.security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${spring.security.jwt.token.expire-length}")
    private long validityInMilliseconds = 3600000;

}
