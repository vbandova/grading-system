package com.interview.roiti.gradingsystem.config;

import com.interview.roiti.gradingsystem.security.JWTAuthorizationFilter;
import com.interview.roiti.gradingsystem.service.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenService tokenService;

    public WebSecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterAfter(new JWTAuthorizationFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .mvcMatchers("/token/teacher", "/token/student/**").permitAll()
                .mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/**", "/webjars/**").permitAll()
                .anyRequest().authenticated();
    }

}
