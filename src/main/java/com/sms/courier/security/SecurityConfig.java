package com.sms.courier.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.security.authentication.AuthenticationFailHandler;
import com.sms.courier.security.authentication.AuthenticationSuccessHandler;
import com.sms.courier.security.authentication.RestAccessDeniedHandler;
import com.sms.courier.security.filter.EngineTokenFilter;
import com.sms.courier.security.filter.UserTokenFilter;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.point.CustomLoginUrlAuthenticationEntryPoint;
import com.sms.courier.service.LogService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final LogService logService;
    private final JwtTokenManager jwtTokenManager;
    private final SecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
        ObjectMapper objectMapper, UserDetailsService userDetailsService,
        LogService logService, JwtTokenManager jwtTokenManager,
        SecurityProperties securityProperties,
        PasswordEncoder passwordEncoder) {
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
        this.logService = logService;
        this.jwtTokenManager = jwtTokenManager;
        this.securityProperties = securityProperties;
        this.passwordEncoder = passwordEncoder;
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationSuccessHandler successHandler = new AuthenticationSuccessHandler(objectMapper, jwtTokenManager,
            logService);
        AuthenticationFailHandler authenticationFailureHandler = new AuthenticationFailHandler(objectMapper);
        RestAccessDeniedHandler accessDeniedHandler = new RestAccessDeniedHandler(objectMapper);
        CustomLoginUrlAuthenticationEntryPoint authenticationEntryPoint = new CustomLoginUrlAuthenticationEntryPoint(
            objectMapper);
        http.csrf().disable()
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and().formLogin()
            .loginProcessingUrl("/v1/auth/login")
            .permitAll()
            .successHandler(successHandler)
            .failureHandler(authenticationFailureHandler)

            .and()
            .authorizeRequests()
            .antMatchers(securityProperties.getIgnorePath().toArray(new String[]{})).permitAll()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint);
        http.headers().cacheControl();
        UserTokenFilter userTokenFilter = new UserTokenFilter(jwtTokenManager);
        EngineTokenFilter engineTokenFilter = new EngineTokenFilter(jwtTokenManager);
        http.addFilterBefore(userTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(engineTokenFilter, UserTokenFilter.class);
    }
}