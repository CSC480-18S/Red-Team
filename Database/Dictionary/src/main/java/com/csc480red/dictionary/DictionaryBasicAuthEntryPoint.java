package com.csc480red.dictionary;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/*
 * Custom Basic Authentication Entry Point
 * Basic entry point to customize 401 errors and messages
 * (Bad credentials / Need authentication errors)
 */

@Component
public class DictionaryBasicAuthEntryPoint extends BasicAuthenticationEntryPoint {
 
	//When sending a request to the database, make sure they are authenticated first
	//If not authorized, throw a 401
    @Override
    public void commence
      (HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) 
      throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - " + authEx.getMessage());
    }
 
    /*
     * Set name of realm for commence (see above)
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("CSC480");
        super.afterPropertiesSet();
    }
}
