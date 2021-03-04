package org.myapp.dev.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.myapp.dev.api.exception.ErrorMessage;
import org.myapp.dev.config.JwtConfig;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public AuthorizationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        Optional<String> token = Optional.ofNullable(req.getHeader(jwtConfig.getAuthorizationHeader()));

        if(!token.isPresent() || !token.get().startsWith(jwtConfig.getTokenPrefix())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken auth = getAuthentication(req, token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(req, res);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req, Optional<String> tokenHolder) {
        String token = tokenHolder.get().replace(jwtConfig.getTokenPrefix(), "");

        try {
            Optional<Jws<Claims>> claimHolder = Optional.ofNullable(Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token));

            if(claimHolder.isEmpty())
                throw new AuthorizationServiceException(String.format("Invalid token %s claims not found", token));

            Claims claimBody = claimHolder.get().getBody();
            String userId = claimBody.getSubject();

            var authorities = (List<Map<String, String>>) claimBody.get(jwtConfig.getAuthoritiesTag());

            Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            return new UsernamePasswordAuthenticationToken(userId, null, grantedAuthorities);

        } catch (JwtException ex) {
            throw new AuthorizationServiceException(ErrorMessage.INVALID_TOKEN.getErrorMessage());
        }
    }
}
