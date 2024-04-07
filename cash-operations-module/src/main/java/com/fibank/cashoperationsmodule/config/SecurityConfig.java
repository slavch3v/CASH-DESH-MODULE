package com.fibank.cashoperationsmodule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<APIKeyAuthFilter> apiKeyAuthFilterRegistrationBean() {
        FilterRegistrationBean<APIKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new APIKeyAuthFilter());
        registrationBean.addUrlPatterns("/api/v1/*");
        return registrationBean;
    }
}
