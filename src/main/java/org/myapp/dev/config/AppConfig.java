package org.myapp.dev.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.properties")
public class AppConfig {

    private String userIdHeader;
    private String signUpUrl;
    private String loginUrl;
    private String h2consoleUrl;
    private Boolean h2Enabled;
    private String swaggerUrl;
}
