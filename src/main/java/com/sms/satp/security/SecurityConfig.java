package com.sms.satp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.security.authentication.AuthenticationFailHandler;
import com.sms.satp.security.authentication.AuthenticationSuccessHandler;
import com.sms.satp.security.authentication.RestAccessDeniedHandler;
import com.sms.satp.security.filter.EngineTokenFilter;
import com.sms.satp.security.filter.UserTokenFilter;
import com.sms.satp.security.jwt.JwtTokenManager;
import com.sms.satp.security.point.CustomLoginUrlAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final JwtTokenManager jwtTokenManager;
    private final SecurityProperties securityProperties;

    public SecurityConfig(
        ObjectMapper objectMapper, UserDetailsService userDetailsService,
        JwtTokenManager jwtTokenManager, SecurityProperties securityProperties) {
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
        this.jwtTokenManager = jwtTokenManager;
        this.securityProperties = securityProperties;
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationSuccessHandler successHandler = new AuthenticationSuccessHandler(objectMapper, jwtTokenManager);
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