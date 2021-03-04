package org.myapp.dev.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "app.jwt.security")
@Data
public class JwtConfig {

    private String secretKey;
    private String expirationDay;
    private String tokenPrefix;
    private String authoritiesTag;

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}
