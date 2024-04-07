package com.fibank.cashoperationsmodule.config;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class APIKeyAuthFilter implements Filter {

    private static final String API_KEY = "f9Uie8nNf112hx8s";
    private static final String API_KEY_HEADER_NAME = "FIB-X-AUTH";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String apiKey = httpRequest.getHeader(API_KEY_HEADER_NAME);
        // Добавете логиране тук
        System.out.println("API Key Received: " + apiKey);
        if (API_KEY.equals(apiKey)) {
            System.out.println("vsichko e okeii");
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Invalid API key");
            // Още логиране тук
            System.out.println("Invalid API key received");
        }
    }

}
