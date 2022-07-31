package com.b2.b2data.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // cross site request forgery
        // all methods resulting in data change (POST, DELETE, etc.) are blocked by default
        // by spring security; ignore for selected paths
        http.csrf().ignoringAntMatchers("/h2-console/**");

        // TODO: replace with .mvcMatchers(...).authenticated()/.permitAll() for each page
        // for any request, require authentication
        // default username is user, password is printed on console
        http.authorizeHttpRequests().anyRequest().authenticated();

        // allows credentials via login form
        // use .loginPage(String loginPage) to use a custom login page
        http.formLogin();

        // allows credentials via http headers
        http.httpBasic(withDefaults());

        // allows h2-console to display in a frame, which is blocked by default in spring security
        // .sameOrigin() allows page to be displayed in a frame on the same origin as the page itself
        // should not be used in production (h2 not used in production)
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
