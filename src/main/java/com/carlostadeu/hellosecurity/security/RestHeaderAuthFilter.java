package com.carlostadeu.hellosecurity.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Slf4j
public class RestHeaderAuthFilter extends AbstractAuthenticationProcessingFilter {

    protected RestHeaderAuthFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String user = getUser(request);
        String pass = getPass(request);

        if (user == null)
            user = "";
        if (pass == null)
            pass = "";

        log.debug("Authentication User: " + user);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, pass);
        return this.getAuthenticationManager().authenticate(token);
    }

    private String getPass(HttpServletRequest request) {
        return request.getHeader("Api-Secret");
    }

    private String getUser(HttpServletRequest request) {
        return request.getHeader("Api-Key");
    }
}
