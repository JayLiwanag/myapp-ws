package org.myapp.dev.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

//Unused class yet
@Component
public class CustomAuthenticationException implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
                                        throws IOException, ServletException {

        Map<String, Object> data = new HashMap<>();

        data.put( "timestamp",  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put( "status",  HttpStatus.UNAUTHORIZED.value());
        data.put( "message", "Authentication failed.");
        data.put( "path", "/users/login");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(response.SC_UNAUTHORIZED);

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, data);

        out.flush();
    }
}