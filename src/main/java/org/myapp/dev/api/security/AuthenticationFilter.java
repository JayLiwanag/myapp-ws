package org.myapp.dev.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.myapp.dev.api.exception.AuthenticationException;
import org.myapp.dev.api.exception.ErrorMessage;
import org.myapp.dev.config.AppConfig;
import org.myapp.dev.config.JwtConfig;
import org.myapp.dev.core.user.model.request.UserLoginDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;
    private final AppConfig appConfig;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AppConfig appConfig, JwtConfig jwtConfig, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.appConfig = appConfig;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }


    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws org.springframework.security.core.AuthenticationException {

        UserLoginDetails credentials = new ObjectMapper().readValue(req.getInputStream(), UserLoginDetails.class);

        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword()));
        }
        catch (Exception ex) {
            throw new AuthenticationException(String.format("User : '%s' ", credentials.getUsername()).concat(ErrorMessage.AUTHENTICATION_FAILED.getErrorMessage()), true);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(auth.getName()) // userId
                .claim(jwtConfig.getAuthoritiesTag(), auth.getAuthorities()) // privilege list
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.setHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        res.setHeader(appConfig.getUserIdHeader(), auth.getName());
    }
}
