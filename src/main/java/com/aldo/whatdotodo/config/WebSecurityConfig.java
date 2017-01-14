package com.aldo.whatdotodo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders
    .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration
    .WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .formLogin()
            .and()

            .logout();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("user-1").password("password-1").roles("USER").and()
            .withUser("user-2").password("password-2").roles("USER").and()
            .withUser("user-3").password("password-3").roles("USER");
    }
}
