package com.helseapps.task.config;

import com.helseapps.task.rest.entity.User;
import com.helseapps.task.rest.exception.AuthenticationException;
import com.helseapps.task.rest.service.EncryptionService;
import com.helseapps.task.rest.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
public class BasicAuthorizationFilter extends BasicAuthenticationFilter {
    public static final String TOKEN_BASIC_PREFIX = "Basic ";
    public static final String HEADER_STRING = "Authorization";

    @Inject
    private UserService userService;

    public BasicAuthorizationFilter(AuthenticationManager authManager, UserService userService) {
        super(authManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_BASIC_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token.startsWith(TOKEN_BASIC_PREFIX)) {
            return getAuthFromBasic(request);
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken getAuthFromBasic(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String credentials = token.replace(TOKEN_BASIC_PREFIX, "");
            // decoding credentials
            String[] decodedCredentials = new String(
                    Base64.getDecoder()
                            .decode(credentials)
            ).split(":");

            // verifying if the credentials received are valid
            if (Strings.isNotEmpty(decodedCredentials[0]) &&
                    Strings.isNotEmpty(decodedCredentials[1])) {
                // user retrieving logic
                User user = userService.getUserByUsernameAndPassword(decodedCredentials[0], EncryptionService.encrypt(decodedCredentials[1]));
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken("", credentials, null);
                }
            }
        }
        return null;
    }
}